package com.warlock.controller;

import com.warlock.factory.RoleFactoryDTO;
import com.warlock.factory.UserFactoryDTO;
import com.warlock.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.warlock.service.UserService;
import com.warlock.model.request.CreateUserRequest;
import com.warlock.model.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final RoleService roleService;

    @Autowired
    private final UserFactoryDTO userFactoryDTO;

    @Autowired
    private final RoleFactoryDTO roleFactoryDTO;

    @GetMapping(produces=APPLICATION_JSON_VALUE)
    public List<UserResponse> findAll(){
        return userService.findAll()
                .stream()
                .map(user ->{
                    var roleResponse = roleFactoryDTO.fromEntityToDTO(user.getRole());
                    return userFactoryDTO.fromEntityToDTO(user, roleResponse);
                })
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{userId}", produces=APPLICATION_JSON_VALUE)
    public UserResponse findById(@PathVariable Long userId){
        var user = userService.findById(userId);
        return userFactoryDTO.fromEntityToDTO(user, roleFactoryDTO.fromEntityToDTO(user.getRole()));
    }

    @PostMapping(value = "/registration", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserResponse create(@RequestBody CreateUserRequest request) {
        var user = userFactoryDTO.fromDTOToEntity(request);
        var role = request.getRole().getName();
        user.setRole(role != null ? roleService.findByName(role) : null);

        var createdUser = userService.createUser(user);

        return userFactoryDTO.fromEntityToDTO(createdUser, roleFactoryDTO.fromEntityToDTO(createdUser.getRole()));
    }

    @PatchMapping(value = "/{userId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public UserResponse update(@PathVariable Long userId, @RequestBody CreateUserRequest request) {
        var user = userFactoryDTO.fromDTOToEntity(request);
        var role = request.getRole().getName();
        user.setRole(role != null ? roleService.findByName(role) : null);

        var updatedUser = userService.update(userId, user);

        return userFactoryDTO.fromEntityToDTO(updatedUser, roleFactoryDTO.fromEntityToDTO(updatedUser.getRole()));
    }

    @DeleteMapping(value = "/{userId}", produces = APPLICATION_JSON_VALUE)
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }
}