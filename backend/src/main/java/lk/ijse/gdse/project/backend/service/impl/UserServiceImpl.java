package lk.ijse.gdse.project.backend.service.impl;

import lk.ijse.gdse.project.backend.dto.UserDTO;
import lk.ijse.gdse.project.backend.entity.User;
import lk.ijse.gdse.project.backend.exception.ResourceNotFoundException;
import lk.ijse.gdse.project.backend.repository.UserRepository;
import lk.ijse.gdse.project.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

}
