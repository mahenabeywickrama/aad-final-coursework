package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.APIResponse;
import lk.ijse.gdse.project.backend.dto.UserDTO;
import lk.ijse.gdse.project.backend.entity.User;
import lk.ijse.gdse.project.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/admin/user")
public class AdminUserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public AdminUserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("getAll")
    public ResponseEntity<APIResponse> getAllUsers() {
        List<UserDTO> userDTOS = userService.getUsers();
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Success",
                        userDTOS
                )
        );
    }

    @GetMapping("getFromPage")
    public ResponseEntity<APIResponse> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String searchValue) {

        Page<User> userPage;

        if (searchValue != null && !searchValue.isEmpty()) {
            userPage = userService.searchUsers(searchValue, PageRequest.of(page, size));
        } else {
            userPage = userService.getAllUsers(PageRequest.of(page, size));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", userPage.getContent());
        response.put("totalItems", userPage.getTotalElements());

        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Success",
                        response
                )
        );
    }

}
