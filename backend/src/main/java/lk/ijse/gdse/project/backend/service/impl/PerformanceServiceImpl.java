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

@Service
public class PerformanceServiceImpl implements PerformanceService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Integer> getGameFPS(String cpu, String gpu, String ram, String storage) {
        // This method returns 1080p data for backward compatibility
        Map<String, Map<String, Integer>> allResolutions = getGameFPSAllResolutions(cpu, gpu, ram, storage);
        return allResolutions.getOrDefault("1080p", getFallbackFPSForResolution("1080p"));
    }

    public Map<String, Map<String, Integer>> getGameFPSAllResolutions(String cpu, String gpu, String ram, String storage) {
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

            Map<String, Map<String, Integer>> allResolutionsFPS = objectMapper.readValue(text, new TypeReference<>() {});
            return validateAndReturnMultiResFPS(allResolutionsFPS);

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
        Map<String, Map<String, Integer>> result = new HashMap<>();

        List<String> resolutions = Arrays.asList("1080p", "1440p", "4K");

        for (String resolution : resolutions) {
            Map<String, Integer> resFPS = allResolutionsFPS.get(resolution);
            if (resFPS != null && resFPS.size() >= 3) {
                // Filter out invalid FPS values
                Map<String, Integer> validFps = new HashMap<>();
                for (Map.Entry<String, Integer> entry : resFPS.entrySet()) {
                    int fpsValue = entry.getValue();
                    if (fpsValue > 0 && fpsValue <= 300) {
                        validFps.put(entry.getKey(), fpsValue);
                    }
                }

                if (validFps.size() >= 3) {
                    // Keep only first 3 entries
                    Map<String, Integer> limitedFps = new HashMap<>();
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
                Map.of("CS 2", 140, "COD MW3", 100, "Elden Ring", 70),
                Map.of("MC RTX", 75, "Spider-Man RE", 80, "God of War", 75)
        );

        Random random = new Random();
        Map<String, Integer> selectedBaseFPS = fallbackOptions.get(random.nextInt(fallbackOptions.size()));

        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();

        // 1080p (base values)
        result.put("1080p", new LinkedHashMap<>(selectedBaseFPS));

        // 1440p (70% of 1080p)
        Map<String, Integer> fps1440p = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : selectedBaseFPS.entrySet()) {
            fps1440p.put(entry.getKey(), (int) Math.round(entry.getValue() * 0.7));
        }
        result.put("1440p", fps1440p);

        // 4K (45% of 1080p)
        Map<String, Integer> fps4k = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : selectedBaseFPS.entrySet()) {
            fps4k.put(entry.getKey(), (int) Math.round(entry.getValue() * 0.45));
        }
        result.put("4K", fps4k);

        return result;
    }
}