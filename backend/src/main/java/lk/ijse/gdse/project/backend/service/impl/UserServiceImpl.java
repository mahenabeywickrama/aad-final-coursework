package lk.ijse.gdse.project.backend.service.impl;

import lk.ijse.gdse.project.backend.dto.PCDTO;
import lk.ijse.gdse.project.backend.dto.UserDTO;
import lk.ijse.gdse.project.backend.entity.PC;
import lk.ijse.gdse.project.backend.entity.User;
import lk.ijse.gdse.project.backend.exception.ResourceNotFoundException;
import lk.ijse.gdse.project.backend.repository.UserRepository;
import lk.ijse.gdse.project.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO findByUsername(String username) {
        if (!userRepository.existsUserByUsername(username)) {
            throw new ResourceNotFoundException("Username " + username + " not found");
        }
        return modelMapper.map(userRepository.findByUsername(username).orElse(null), UserDTO.class);
    }

    @Override
    public User getEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserDTO> getUsers() {
        List<User> all = userRepository.findAll();

        if (all.isEmpty()) {
            throw new ResourceNotFoundException("No Users found");
        }

        return modelMapper.map(all, new TypeToken<List<UserDTO>>() {}.getType());
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUsers(String searchValue, Pageable pageable) {
        return userRepository.searchUsers(searchValue, pageable);
    }
}
