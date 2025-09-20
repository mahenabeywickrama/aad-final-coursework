package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.APIResponse;
import lk.ijse.gdse.project.backend.dto.UserDTO;
import lk.ijse.gdse.project.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("find/{username}")
    public ResponseEntity<APIResponse> findUser(@PathVariable("username") String username) {
        UserDTO userByUsername = userService.findByUsername(username);
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "User Found Successfully",
                        userByUsername
                )
        );
    }
}
