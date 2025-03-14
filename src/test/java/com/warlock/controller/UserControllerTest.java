package com.warlock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warlock.model.request.CreateRoleRequest;
import com.warlock.model.request.CreateUserRequest;
import com.warlock.model.response.RoleResponse;
import com.warlock.model.response.UserResponse;
import com.warlock.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // Создание пользователя без роли
    @Test
    void testCreateUserWithoutRole() throws Exception {
        // Подготовка данных
        CreateUserRequest request = new CreateUserRequest()
                .setNickname("user1")
                .setLogin("user_login1")
                .setPassword("password123")
                .setEmail("user1@example.com");

        UserResponse response = new UserResponse()
                .setId(1L)
                .setNickname("user1")
                .setLogin("user_login1")
                .setPassword("password123")
                .setEmail("user1@example.com")
                .setRole(new RoleResponse()
                        .setId(2L)
                        .setName("user"));

        // Мокируем сервис
        when(userService.createUser(request)).thenReturn(response);

        // Выполняем POST-запрос
        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickname").value("user1"))
                .andExpect(jsonPath("$.login").value("user_login1"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.role.id").value(2L))
                .andExpect(jsonPath("$.role.name").value("user"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).createUser(request);
    }

    // Создание пользователя с ролью админа
    @Test
    void testCreateUserWithRoleAdmin() throws Exception {
        // Подготовка данных
        CreateUserRequest request = new CreateUserRequest()
                .setNickname("user2")
                .setLogin("user_login2")
                .setPassword("password123")
                .setEmail("user@example.com")
                .setRole(new CreateRoleRequest()
                        .setName("admin"));

        UserResponse response = new UserResponse()
                .setId(2L)
                .setNickname("user2")
                .setLogin("user_login2")
                .setPassword("password123")
                .setEmail("user2@example.com")
                .setRole(new RoleResponse()
                        .setId(1L)
                        .setName("admin"));

        // Мокируем сервис
        when(userService.createUser(request)).thenReturn(response);

        // Выполняем POST-запрос
        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nickname").value("user2"))
                .andExpect(jsonPath("$.login").value("user_login2"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.email").value("user2@example.com"))
                .andExpect(jsonPath("$.role.id").value(1L))
                .andExpect(jsonPath("$.role.name").value("admin"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).createUser(request);
    }

    // Обновление имени пользователя
    @Test
    void testUpdateUser() throws Exception {
        // Подготовка данных
        Long userId = 1L;
        CreateUserRequest request = new CreateUserRequest()
                .setNickname("updated_user1");

        UserResponse response = new UserResponse()
                .setId(userId)
                .setNickname("updated_user1")
                .setLogin("user_login1")
                .setPassword("password123")
                .setEmail("user1@example.com")
                .setRole(new RoleResponse()
                        .setId(2L)
                        .setName("user"));

        // Мокируем сервис
        when(userService.update(userId, request)).thenReturn(response);

        // Выполняем PATCH-запрос
        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickname").value("updated_user1"))
                .andExpect(jsonPath("$.login").value("user_login1"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.role.id").value(2L))
                .andExpect(jsonPath("$.role.name").value("user"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).update(userId, request);
    }

    // Поиск пользователя 1 по ID
    @Test
    void testFindUserById() throws Exception {
        // Подготовка данных
        Long userId = 1L;
        UserResponse response = new UserResponse()
                .setId(userId)
                .setNickname("updated_user1")
                .setLogin("user_login1")
                .setPassword("password123")
                .setEmail("user1@example.com")
                .setRole(new RoleResponse()
                        .setId(2L)
                        .setName("user"));

        // Мокируем сервис
        when(userService.findById(userId)).thenReturn(response);

        // Выполняем GET-запрос
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nickname").value("updated_user1"))
                .andExpect(jsonPath("$.login").value("user_login1"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.role.id").value(2L))
                .andExpect(jsonPath("$.role.name").value("user"));

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).findById(userId);
    }

    // Удаление пользователя
    @Test
    void testDeleteUser() throws Exception {
        // Подготовка данных
        Long userId = 1L;

        // Выполняем DELETE-запрос
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());

        // Проверяем, что сервис был вызван
        verify(userService, times(1)).delete(userId);
    }
}