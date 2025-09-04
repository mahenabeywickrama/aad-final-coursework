package lk.ijse.gdse.project.backend.dto;

import lk.ijse.gdse.project.backend.entity.Role;

import java.util.Objects;

public class RegisterDTO {
    private String username;
    private String password;
    private String email;
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        if (Objects.equals(role, "ADMIN")) {
            return Role.ADMIN;
        } else {
            return Role.USER;
        }
    }

    public void setRole(String role) {
        this.role = role;
    }
}
