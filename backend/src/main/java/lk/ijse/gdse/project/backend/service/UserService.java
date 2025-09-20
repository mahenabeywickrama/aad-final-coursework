package lk.ijse.gdse.project.backend.service;

import lk.ijse.gdse.project.backend.dto.UserDTO;
import lk.ijse.gdse.project.backend.entity.User;

public interface UserService {
    UserDTO findByUsername(String username);
    User getEntityByUsername(String username);

}
