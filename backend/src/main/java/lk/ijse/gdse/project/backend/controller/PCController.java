package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.APIResponse;
import lk.ijse.gdse.project.backend.dto.PCDTO;
import lk.ijse.gdse.project.backend.entity.PC;
import lk.ijse.gdse.project.backend.service.PCService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/admin/pc")
public class PCController {

    private final PCService pcService;
    private final ModelMapper modelMapper;

    public PCController(PCService pcService, ModelMapper modelMapper) {
        this.pcService = pcService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("create")
    public ResponseEntity<APIResponse> createJob(@RequestBody PCDTO pcdto) {
        pcService.savePC(pcdto);
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "PC Created Successfully",
                        null
                )
        );
    }

    @PutMapping("update")
    public ResponseEntity<APIResponse> updateJob(@RequestBody PCDTO pcdto) {
        pcService.updatePC(pcdto);
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "PC Updated Successfully",
                        null
                )
        );
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<APIResponse> deleteJob(@PathVariable("id") String id) {
        pcService.deletePC(id);
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "PC Deleted Successfully",
                        null
                )
        );
    }

    @GetMapping("getAll")
    public ResponseEntity<APIResponse> getAllJobs() {
        List<PCDTO> pcdtos = pcService.getPCs();
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Success",
                        pcdtos
                )
        );
    }

    @GetMapping("getFromPage")
    public ResponseEntity<APIResponse> getAllPCs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PC> pcPage = pcService.getAllPCs(PageRequest.of(page, size));

        Map<String,Object> response = new HashMap<>();
        response.put("data", pcPage.getContent());
        response.put("totalItems", pcPage.getTotalElements());
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Success",
                        response
                )
        );
    }

}
