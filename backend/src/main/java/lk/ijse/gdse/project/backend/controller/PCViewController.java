package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.APIResponse;
import lk.ijse.gdse.project.backend.dto.PCDTO;
import lk.ijse.gdse.project.backend.entity.PC;
import lk.ijse.gdse.project.backend.service.PCService;
import lk.ijse.gdse.project.backend.service.PerformanceService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/pc")
public class PCViewController {
    private final PCService pcService;
    private final PerformanceService performanceService;
    private final ModelMapper modelMapper;

    public PCViewController(PCService pcService, PerformanceService performanceService, ModelMapper modelMapper) {
        this.pcService = pcService;
        this.performanceService = performanceService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PCDTO>> getAllPCs() {
        List<PCDTO> pcs = pcService.getPCs();
        return ResponseEntity.ok(pcs);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<APIResponse> findPC(@PathVariable("id") String id) {
        PCDTO pcById = pcService.findPCById(id);
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "PC Found Successfully",
                        pcById
                )
        );
    }

    @GetMapping("/{id}/fps")
    public ResponseEntity<APIResponse> getFPS(@PathVariable String id) {
        try {
            PC pc = modelMapper.map(pcService.findPCById(id), PC.class);
            if (pc == null) {
                return ResponseEntity.status(404).body(
                        new APIResponse(404, "PC not found", null)
                );
            }

            // Get multi-resolution FPS data
            Map<String, Map<String, Integer>> allResolutionsFPS = performanceService.getGameFPSAllResolutions(
                    pc.getCpu(), pc.getGpu(), pc.getRam(), pc.getStorage()
            );

            return ResponseEntity.ok(new APIResponse(
                    200,
                    "FPS data retrieved successfully",
                    allResolutionsFPS
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new APIResponse(500, "Error retrieving FPS data: " + e.getMessage(), null)
            );
        }
    }
}
