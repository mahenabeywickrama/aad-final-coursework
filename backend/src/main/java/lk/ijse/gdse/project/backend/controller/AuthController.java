package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.APIResponse;
import lk.ijse.gdse.project.backend.dto.AuthDTO;
import lk.ijse.gdse.project.backend.dto.RegisterDTO;
import lk.ijse.gdse.project.backend.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthServiceImpl authServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse> registerUser(
            @RequestBody RegisterDTO registerDTO){
        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "User registered successfully",
                        authServiceImpl.register(registerDTO)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody AuthDTO authDTO){
        return ResponseEntity.ok(new APIResponse(200,
                "OK", authServiceImpl.authenticate(authDTO)));
    }
}
