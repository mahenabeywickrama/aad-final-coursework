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
    public ResponseEntity<APIResponse> createPC(@RequestBody PCDTO pcdto) {
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
    public ResponseEntity<APIResponse> updatePC(@RequestBody PCDTO pcdto) {
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
    public ResponseEntity<APIResponse> deletePC(@PathVariable("id") String id) {
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
    public ResponseEntity<APIResponse> getAllPCs() {
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
    public ResponseEntity<APIResponse> getPCs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String searchValue) {

        Page<PC> pcPage;

        if (searchValue != null && !searchValue.isEmpty()) {
            pcPage = pcService.searchPCs(searchValue, PageRequest.of(page, size));
        } else {
            pcPage = pcService.getAllPCs(PageRequest.of(page, size));
        }

        Map<String, Object> response = new HashMap<>();
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
