package lk.ijse.gdse.project.backend.service;

import lk.ijse.gdse.project.backend.dto.AuthDTO;
import lk.ijse.gdse.project.backend.dto.AuthResponseDTO;
import lk.ijse.gdse.project.backend.dto.RegisterDTO;

public interface AuthService {
    AuthResponseDTO authenticate(AuthDTO authDTO);
    String register(RegisterDTO registerDTO);
}
