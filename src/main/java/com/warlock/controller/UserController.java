package com.warlock.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.warlock.service.UserService;
import com.warlock.model.request.CreateUserRequest;
import com.warlock.model.response.UserResponse;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces=APPLICATION_JSON_VALUE)
    public List<UserResponse> findAll(){
        return userService.findAll();
    }

    @GetMapping(value = "/{userId}", produces=APPLICATION_JSON_VALUE)
    public UserResponse findById(@PathVariable Long userId){
        return userService.findById(userId);
    }

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PatchMapping(value = "/{userId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserResponse update(@PathVariable Long userId, @RequestBody CreateUserRequest request) {
        return userService.update(userId, request);
    }

    //Удаляем пользователя по id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }
}