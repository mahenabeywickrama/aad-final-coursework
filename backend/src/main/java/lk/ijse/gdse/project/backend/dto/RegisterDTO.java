package lk.ijse.gdse.project.backend.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String email;
    private String role;
}
