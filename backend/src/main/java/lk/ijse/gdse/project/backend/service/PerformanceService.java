package lk.ijse.gdse.project.backend.service;

import java.util.Map;

public interface PerformanceService {
    Map<String, Integer> getGameFPS(String cpu, String gpu, String ram, String storage);
    Map<String, Map<String, Integer>> getGameFPSAllResolutions(String cpu, String gpu, String ram, String storage);
}
