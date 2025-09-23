package lk.ijse.gdse.project.backend.service;

import lk.ijse.gdse.project.backend.dto.UserDTO;
import lk.ijse.gdse.project.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDTO findByUsername(String username);
    User getEntityByUsername(String username);
    List<UserDTO> getUsers();
    Page<User> getAllUsers(Pageable pageable);
    Page<User> searchUsers(String searchValue, Pageable pageable);
}
