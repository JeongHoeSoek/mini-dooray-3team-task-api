package com.nhnacademy.minidooray3teamaccountapi.user;

import com.nhnacademy.minidooray3teamaccountapi.controller.UserController;
import com.nhnacademy.minidooray3teamaccountapi.dto.UserDTO;
import com.nhnacademy.minidooray3teamaccountapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_Success() {
        // Given
        UserDTO userDTO = new UserDTO("user1", "John Doe", "john.doe@example.com");
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        // When
        ResponseEntity<UserDTO> response = userController.createUser(userDTO);

        // Then
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(userDTO, response.getBody());
    }
}
