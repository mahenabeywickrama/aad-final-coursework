package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.APIResponse;
import lk.ijse.gdse.project.backend.dto.DashboardPCStatsDTO;
import lk.ijse.gdse.project.backend.service.DashboardPCService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/pcdashboard")
public class DashboardPCController {
    private final DashboardPCService dashboardPCService;

    public DashboardPCController(DashboardPCService dashboardPCService) {
        this.dashboardPCService = dashboardPCService;
    }

    @GetMapping("/stats")
    public ResponseEntity<APIResponse> getStats() {
        DashboardPCStatsDTO dashboardPCStats = dashboardPCService.getDashboardPCStats();
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Stats Received Successfully",
                        dashboardPCStats
                )
        );
    }
}
