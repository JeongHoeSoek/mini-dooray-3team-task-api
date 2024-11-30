package com.nhnacademy.minidooray3teamaccountapi.user;

import com.nhnacademy.minidooray3teamaccountapi.dto.UserDTO;
import com.nhnacademy.minidooray3teamaccountapi.entity.User;
import com.nhnacademy.minidooray3teamaccountapi.repository.UserRepository;
import com.nhnacademy.minidooray3teamaccountapi.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_Success() {
        // Given
        UserDTO userDTO = new UserDTO("user1", "John Doe", "john.doe@example.com");
        User savedUser = new User("user1", "John Doe", "john.doe@example.com", LocalDateTime.now(), null);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserDTO result = userService.createUser(userDTO);

        // Then
        assertEquals(userDTO.getUserId(), result.getUserId());
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getEmail(), result.getEmail());

        verify(userRepository).save(any(User.class));
    }
}
