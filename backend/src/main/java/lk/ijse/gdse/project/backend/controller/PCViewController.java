package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.APIResponse;
import lk.ijse.gdse.project.backend.dto.PCDTO;
import lk.ijse.gdse.project.backend.service.PCService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/pc")
public class PCViewController {
    private final PCService pcService;

    public PCViewController(PCService pcService) {
        this.pcService = pcService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PCDTO>> getAllPCs() {
        List<PCDTO> pcs = pcService.getPCs();
        return ResponseEntity.ok(pcs);
    }

    @GetMapping("find/{id}")
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
}
