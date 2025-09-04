package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.APIResponse;
import lk.ijse.gdse.project.backend.dto.PCDTO;
import lk.ijse.gdse.project.backend.service.PCService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/job")
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
}
