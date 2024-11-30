package com.nhnacademy.minidooray3teamaccountapi.service;

import com.nhnacademy.minidooray3teamaccountapi.dto.UserDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.User;
import com.nhnacademy.minidooray3teamaccountapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User(
            userDTO.getUserId(),
            userDTO.getUsername(),
            userDTO.getEmail(),
            LocalDateTime.now(),
            null
        );
        userRepository.save(user);
        return userDTO;
    }
}
