package com.warlock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warlock.model.request.CreateUserRequest;
import com.warlock.model.response.UserResponse;
import com.warlock.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUser() throws Exception {
        // Подготовка данных
        CreateUserRequest request = new CreateUserRequest()
                .setNickname("user123")
                .setLogin("user_login")
                .setPassword("password123")
                .setEmail("user@example.com");

        UserResponse response = new UserResponse()
                .setId(1L)
                .setNickname("user123")
                .setLogin("user_login")
                .setEmail("user@example.com");

        // Мокируем сервис
        when(userService.createUser(request)).thenReturn(response);

        // Выполняем POST-запрос
        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickname").value("user123"))
                .andExpect(jsonPath("$.login").value("user_login"))
                .andExpect(jsonPath("$.email").value("user@example.com"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).createUser(request);
    }

    @Test
    void testUpdateUser() throws Exception {
        // Подготовка данных
        Long userId = 1L;
        CreateUserRequest request = new CreateUserRequest()
                .setNickname("updated_user")
                .setLogin("updated_login")
                .setPassword("new_password")
                .setEmail("updated@example.com");

        UserResponse response = new UserResponse()
                .setId(userId)
                .setNickname("updated_user")
                .setLogin("updated_login")
                .setEmail("updated@example.com");

        // Мокируем сервис
        when(userService.update(userId, request)).thenReturn(response);

        // Выполняем PUT-запрос
        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nickname").value("updated_user"))
                .andExpect(jsonPath("$.login").value("updated_login"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).update(userId, request);
    }

    @Test
    void testFindUserById() throws Exception {
        // Подготовка данных
        Long userId = 1L;
        UserResponse response = new UserResponse()
                .setId(userId)
                .setNickname("user123")
                .setLogin("user_login")
                .setEmail("user@example.com");

        // Мокируем сервис
        when(userService.findById(userId)).thenReturn(response);

        // Выполняем GET-запрос
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nickname").value("user123"))
                .andExpect(jsonPath("$.login").value("user_login"))
                .andExpect(jsonPath("$.email").value("user@example.com"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void testDeleteUser() throws Exception {
        // Подготовка данных
        Long userId = 1L;

        // Выполняем DELETE-запрос
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).delete(userId);
    }
}