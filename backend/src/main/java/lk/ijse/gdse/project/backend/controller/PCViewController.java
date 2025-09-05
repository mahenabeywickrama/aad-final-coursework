package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.PCDTO;
import lk.ijse.gdse.project.backend.service.PCService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
