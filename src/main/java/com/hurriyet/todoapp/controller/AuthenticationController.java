package com.hurriyet.todoapp.controller;

import com.hurriyet.todoapp.exception.NotFoundException;
import com.hurriyet.todoapp.util.MapperUtil;
import com.hurriyet.todoapp.model.data.User;
import com.hurriyet.todoapp.model.request.LoginRequest;
import com.hurriyet.todoapp.model.request.RegisterUserRequest;
import com.hurriyet.todoapp.model.response.UserResponse;
import com.hurriyet.todoapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class AuthenticationController {

    private final UserService userService;
    private final MapperUtil mapperUtil;

    public AuthenticationController(UserService userService, MapperUtil mapperUtil) {
        this.userService = userService;
        this.mapperUtil = mapperUtil;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterUserRequest userRequest) throws Exception {

        Optional<User> exists = userService.findUserByUsername(userRequest.getUsername());
        if (exists.isPresent()) {
            throw new NotFoundException("username already exists");
        }

        User user = mapperUtil.mapToUser(userRequest);
        User registeredUser = userService.registerUser(user);

        return ResponseEntity.ok(mapperUtil.mapToUserResponse(registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest) throws NotFoundException {

        User user = mapperUtil.mapToUser(loginRequest);
        Optional<User> userOptional = userService.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        return userOptional.map(use -> ResponseEntity.ok(mapperUtil.mapToUserResponse(use)) )
                .orElseThrow(() -> new NotFoundException("username not found or password not matched"));
    }





}
