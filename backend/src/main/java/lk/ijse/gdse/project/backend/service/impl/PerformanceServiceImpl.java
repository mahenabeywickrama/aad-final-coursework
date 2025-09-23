package lk.ijse.gdse.project.backend.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.gdse.project.backend.service.PerformanceService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    // Enhanced caching
    private final Map<String, Map<String, Map<String, Integer>>> cache = new ConcurrentHashMap<>();
    private final Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = 7 * 24 * 60 * 60 * 1000L; // 7 days

    @Override
    public Map<String, Integer> getGameFPS(String cpu, String gpu, String ram, String storage) {
        Map<String, Map<String, Integer>> allResolutions = getGameFPSAllResolutions(cpu, gpu, ram, storage);
        return allResolutions.getOrDefault("1080p", getFallbackFPSForResolution("1080p"));
    }

    public Map<String, Map<String, Integer>> getGameFPSAllResolutions(String cpu, String gpu, String ram, String storage) {
        String cacheKey = generateCacheKey(cpu, gpu, ram, storage);

        // Check cache first
        if (cache.containsKey(cacheKey)) {
            Long timestamp = cacheTimestamps.get(cacheKey);
            if (timestamp != null && (System.currentTimeMillis() - timestamp) < CACHE_DURATION) {
                System.out.println("Using cached FPS data for: " + cacheKey);
                return cache.get(cacheKey);
            } else {
                // Remove expired cache
                cache.remove(cacheKey);
                cacheTimestamps.remove(cacheKey);
            }
        }

        // Try API call
        Map<String, Map<String, Integer>> result = callGeminiAPI(cpu, gpu, ram, storage);

        // Cache the result
        cache.put(cacheKey, result);
        cacheTimestamps.put(cacheKey, System.currentTimeMillis());

        return result;
    }

    private String generateCacheKey(String cpu, String gpu, String ram, String storage) {
        // Create a normalized key to improve cache hit rate
        String normalizedCpu = normalizeCpuName(cpu);
        String normalizedGpu = normalizeGpuName(gpu);
        String normalizedRam = normalizeRamSize(ram);

        return String.format("%s|%s|%s|%s", normalizedCpu, normalizedGpu, normalizedRam, storage)
                .toLowerCase().replaceAll("\\s+", " ");
    }

    private String normalizeCpuName(String cpu) {
        if (cpu == null) return "unknown";
        // Group similar CPUs together for better caching
        cpu = cpu.toLowerCase();
        if (cpu.contains("i5-12") || cpu.contains("i5 12")) return "intel-i5-12th";
        if (cpu.contains("i7-12") || cpu.contains("i7 12")) return "intel-i7-12th";
        if (cpu.contains("i5-13") || cpu.contains("i5 13")) return "intel-i5-13th";
        if (cpu.contains("i7-13") || cpu.contains("i7 13")) return "intel-i7-13th";
        if (cpu.contains("ryzen 5 7")) return "amd-ryzen5-7000";
        if (cpu.contains("ryzen 7 7")) return "amd-ryzen7-7000";
        return cpu.replaceAll("[^a-z0-9]", "");
    }

    private String normalizeGpuName(String gpu) {
        if (gpu == null) return "unknown";
        // Group similar GPUs together
        gpu = gpu.toLowerCase();
        if (gpu.contains("rtx 4060")) return "rtx-4060";
        if (gpu.contains("rtx 4070")) return "rtx-4070";
        if (gpu.contains("rtx 4080")) return "rtx-4080";
        if (gpu.contains("rtx 4090")) return "rtx-4090";
        if (gpu.contains("rtx 3060")) return "rtx-3060";
        if (gpu.contains("rtx 3070")) return "rtx-3070";
        if (gpu.contains("rtx 3080")) return "rtx-3080";
        if (gpu.contains("rx 7600")) return "rx-7600";
        if (gpu.contains("rx 7700")) return "rx-7700";
        if (gpu.contains("rx 7800")) return "rx-7800";
        return gpu.replaceAll("[^a-z0-9]", "");
    }

    private String normalizeRamSize(String ram) {
        if (ram == null) return "unknown";
        // Extract RAM size
        if (ram.toLowerCase().contains("16")) return "16gb";
        if (ram.toLowerCase().contains("32")) return "32gb";
        if (ram.toLowerCase().contains("8")) return "8gb";
        if (ram.toLowerCase().contains("64")) return "64gb";
        return ram.toLowerCase();
    }

    private Map<String, Map<String, Integer>> callGeminiAPI(String cpu, String gpu, String ram, String storage) {
        if (geminiApiKey == null || geminiApiKey.trim().isEmpty()) {
            System.err.println("Gemini API key is not configured");
            return getFallbackFPSAllResolutions();
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + geminiApiKey;

        try {
            // Pre-select 3 random games to ensure consistency across all resolutions
            List<String> gamesList = Arrays.asList(
                    "Cyberpunk 2077", "Apex Legends", "Fortnite", "Valorant",
                    "Call of Duty: Modern Warfare III", "Counter-Strike 2", "Minecraft RTX",
                    "GTA V", "Red Dead Redemption 2", "The Witcher 3", "Elden Ring",
                    "Hogwarts Legacy", "Spider-Man Remastered", "God of War", "Baldur's Gate 3",
                    "Starfield", "Palworld", "Helldivers 2", "Black Myth: Wukong"
            );

            Collections.shuffle(gamesList);
            List<String> selectedGames = gamesList.subList(0, 3);

            Map<String, Object> requestBody = buildGeminiRequestBodyMultiRes(cpu, gpu, ram, storage, selectedGames);
            HttpHeaders headers = buildHeaders();
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            System.out.println("Gemini Response Status: " + response.getStatusCode());
            System.out.println("Gemini Response Body: " + response.getBody());

            return parseGeminiResponseMultiRes(response.getBody());

        } catch (HttpClientErrorException e) {
            System.err.println("Gemini HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());

            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                System.err.println("Quota exceeded! Using fallback data. Consider upgrading your Gemini plan.");
            }

            return getFallbackFPSAllResolutions();
        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            return getFallbackFPSAllResolutions();
        }
    }

    private Map<String, Object> buildGeminiRequestBodyMultiRes(String cpu, String gpu, String ram, String storage, List<String> selectedGames) {
        Map<String, Object> requestBody = new HashMap<>();

        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.6);
        generationConfig.put("maxOutputTokens", 500);

        String game1 = selectedGames.get(0);
        String game2 = selectedGames.get(1);
        String game3 = selectedGames.get(2);

        Map<String, String> part = new HashMap<>();
        part.put("text", String.format(
                "You are a PC gaming performance expert. Given these specifications:\n" +
                        "CPU: %s\nGPU: %s\nRAM: %s\nStorage: %s\n\n" +
                        "Estimate average FPS for these EXACT 3 games: %s, %s, %s\n" +
                        "For all three resolutions: 1080p high settings, 1440p high settings, and 4K high settings.\n\n" +
                        "Consider GPU VRAM limitations, CPU bottlenecks, and realistic performance scaling. " +
                        "Respond with ONLY valid JSON (no markdown, no extra text) in this exact format:\n" +
                        "{\n" +
                        "  \"1080p\": {\"%s\": 85, \"%s\": 120, \"%s\": 60},\n" +
                        "  \"1440p\": {\"%s\": 65, \"%s\": 90, \"%s\": 45},\n" +
                        "  \"4K\": {\"%s\": 35, \"%s\": 50, \"%s\": 25}\n" +
                        "}",
                cpu, gpu, ram, storage,
                game1, game2, game3,
                game1, game2, game3,
                game1, game2, game3,
                game1, game2, game3
        ));

        Map<String, Object> content = new HashMap<>();
        content.put("parts", Collections.singletonList(part));

        requestBody.put("contents", Collections.singletonList(content));
        requestBody.put("generationConfig", generationConfig);

        return requestBody;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private Map<String, Map<String, Integer>> parseGeminiResponseMultiRes(String responseBody) {
        try {
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<>() {});

            if (responseMap.containsKey("error")) {
                System.err.println("Gemini API Error: " + responseMap.get("error"));
                return getFallbackFPSAllResolutions();
            }

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                System.err.println("No candidates in Gemini response");
                return getFallbackFPSAllResolutions();
            }

            Map<String, Object> firstCandidate = candidates.get(0);
            Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
            List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
            String text = parts.get(0).get("text").trim();

            System.out.println("Gemini AI Response: " + text);

            // Clean up response
            text = cleanJsonResponse(text);

            // Use LinkedHashMap to preserve order
            ObjectMapper orderedMapper = new ObjectMapper();
            Map<String, LinkedHashMap<String, Integer>> allResolutionsFPS =
                    orderedMapper.readValue(text, new TypeReference<Map<String, LinkedHashMap<String, Integer>>>() {});

            // Convert to regular Map<String, Map<String, Integer>> while preserving order
            Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
            for (Map.Entry<String, LinkedHashMap<String, Integer>> entry : allResolutionsFPS.entrySet()) {
                result.put(entry.getKey(), new LinkedHashMap<>(entry.getValue()));
            }

            return validateAndReturnMultiResFPS(result);

        } catch (Exception e) {
            System.err.println("Error parsing Gemini multi-resolution response: " + e.getMessage());
            e.printStackTrace();
            return getFallbackFPSAllResolutions();
        }
    }

    private String cleanJsonResponse(String text) {
        // Remove markdown formatting - handle newlines properly
        text = text.trim();

        if (text.startsWith("```json\n")) {
            text = text.substring(8); // Remove "```json\n"
        } else if (text.startsWith("```json")) {
            text = text.substring(7); // Remove "```json"
        } else if (text.startsWith("```\n")) {
            text = text.substring(4); // Remove "```\n"
        } else if (text.startsWith("```")) {
            text = text.substring(3); // Remove "```"
        }

        if (text.endsWith("\n```")) {
            text = text.substring(0, text.length() - 4); // Remove "\n```"
        } else if (text.endsWith("```")) {
            text = text.substring(0, text.length() - 3); // Remove "```"
        }

        return text.trim();
    }

    private Map<String, Map<String, Integer>> validateAndReturnMultiResFPS(Map<String, Map<String, Integer>> allResolutionsFPS) {
        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();

        List<String> resolutions = Arrays.asList("1080p", "1440p", "4K");

        for (String resolution : resolutions) {
            Map<String, Integer> resFPS = allResolutionsFPS.get(resolution);
            if (resFPS != null && resFPS.size() >= 3) {
                // Filter out invalid FPS values while preserving order
                Map<String, Integer> validFps = new LinkedHashMap<>();
                for (Map.Entry<String, Integer> entry : resFPS.entrySet()) {
                    int fpsValue = entry.getValue();
                    if (fpsValue > 0 && fpsValue <= 300) {
                        validFps.put(entry.getKey(), fpsValue);
                    }
                }

                if (validFps.size() >= 3) {
                    // Keep only first 3 entries while preserving order
                    Map<String, Integer> limitedFps = new LinkedHashMap<>();
                    int count = 0;
                    for (Map.Entry<String, Integer> entry : validFps.entrySet()) {
                        if (count < 3) {
                            limitedFps.put(entry.getKey(), entry.getValue());
                            count++;
                        }
                    }
                    result.put(resolution, limitedFps);
                } else {
                    result.put(resolution, getFallbackFPSForResolution(resolution));
                }
            } else {
                result.put(resolution, getFallbackFPSForResolution(resolution));
            }
        }

        return result;
    }

    private Map<String, Integer> getFallbackFPSForResolution(String resolution) {
        // Base FPS values at 1080p
        Map<String, Integer> baseFPS = Map.of(
                "Apex Legends", 120,
                "Cyberpunk 2077", 60,
                "Valorant", 150
        );

        // Scale based on resolution
        double scalingFactor;
        switch (resolution) {
            case "1080p":
                scalingFactor = 1.0;
                break;
            case "1440p":
                scalingFactor = 0.7;  // ~30% performance drop
                break;
            case "4K":
                scalingFactor = 0.45; // ~55% performance drop
                break;
            default:
                scalingFactor = 1.0;
        }

        Map<String, Integer> scaledFPS = new HashMap<>();
        for (Map.Entry<String, Integer> entry : baseFPS.entrySet()) {
            scaledFPS.put(entry.getKey(), (int) Math.round(entry.getValue() * scalingFactor));
        }

        return scaledFPS;
    }

    private Map<String, Map<String, Integer>> getFallbackFPSAllResolutions() {
        System.out.println("Using fallback FPS data for all resolutions");

        // Select one random set of games for consistency
        List<Map<String, Integer>> fallbackOptions = Arrays.asList(
                Map.of("Apex Legends", 120, "Fortnite", 110, "Valorant", 150),
                Map.of("Cyberpunk 2077", 60, "The Witcher 3", 85, "GTA V", 90),
                Map.of("Counter-Strike 2", 140, "Call of Duty MW3", 100, "Elden Ring", 70),
                Map.of("Minecraft RTX", 75, "Spider-Man Remastered", 80, "God of War", 75)
        );

        Random random = new Random();
        Map<String, Integer> selectedBaseFPS = fallbackOptions.get(random.nextInt(fallbackOptions.size()));

        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();

        // 1080p (base values) - preserve order
        result.put("1080p", new LinkedHashMap<>(selectedBaseFPS));

        // 1440p (70% of 1080p) - preserve same order
        Map<String, Integer> fps1440p = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : selectedBaseFPS.entrySet()) {
            fps1440p.put(entry.getKey(), (int) Math.round(entry.getValue() * 0.7));
        }
        result.put("1440p", fps1440p);

        // 4K (45% of 1080p) - preserve same order
        Map<String, Integer> fps4k = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : selectedBaseFPS.entrySet()) {
            fps4k.put(entry.getKey(), (int) Math.round(entry.getValue() * 0.45));
        }
        result.put("4K", fps4k);

        return result;
    }
}