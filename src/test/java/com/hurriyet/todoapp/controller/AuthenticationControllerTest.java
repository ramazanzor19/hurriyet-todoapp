package com.hurriyet.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hurriyet.todoapp.model.data.User;
import com.hurriyet.todoapp.model.request.LoginRequest;
import com.hurriyet.todoapp.model.request.RegisterUserRequest;
import com.hurriyet.todoapp.model.response.UserResponse;
import com.hurriyet.todoapp.service.UserService;
import com.hurriyet.todoapp.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private UserService userService;

    @MockBean
    private MapperUtil mapperUtil;

    @Autowired
    private MockMvc mockMvc;

    LoginRequest request;
    RegisterUserRequest registerUserRequest;
    User user;
    UserResponse userResponse;


    @BeforeTestMethod
    void loginSetup() {
        LoginRequest request = new LoginRequest();
        request.setUsername("ramazan");
        request.setPassword("ramazan");
        this.request = request;

        UserResponse userResponse = new UserResponse();
        userResponse.setFirstName("Ramazan");
        userResponse.setLastName("Zor");
        userResponse.setUsername("ramazan");
        this.userResponse = userResponse;

        User user = new User();
        user.setFirstName("Ramazan");
        user.setLastName("Zor");
        user.setUsername("ramazan");
        user.setPassword("ramazan");
        user.setId("user::hash");
        this.user = user;
    }

    @BeforeTestMethod
    void registerSetup() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setFirstName("Ramazan");
        request.setLastName("Zor");
        request.setUsername("ramazan");
        request.setPassword("ramazan");
        this.registerUserRequest = request;

        UserResponse userResponse = new UserResponse();
        userResponse.setFirstName("Ramazan");
        userResponse.setLastName("Zor");
        userResponse.setUsername("ramazan");
        this.userResponse = userResponse;

        User user = new User();
        user.setFirstName("Ramazan");
        user.setLastName("Zor");
        user.setUsername("ramazan");
        user.setPassword("ramazan");
        user.setId("user::hash");
        this.user = user;

    }

    LoginRequest invalidLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("ramo");
        request.setPassword("ramo");
        return request;
    }

    RegisterUserRequest invalidRegister() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("ramo");
        request.setFirstName("Ramazan");
        request.setPassword("ramo123456");
        return request;
    }

    @Test
    public void loginShouldReturnUser() throws Exception {
        loginSetup();

        when(userService.findUserByUsernameAndPassword(request.getUsername(), request.getPassword()))
              .thenReturn(Optional.of(user));

        when(mapperUtil.mapToUser(request)).thenReturn(user);
        when(mapperUtil.mapToUserResponse(any())).thenReturn(userResponse);

        mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userResponse.getUsername()))
                .andExpect(jsonPath("$.firstName").value(userResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userResponse.getLastName()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void whenLoginInputIsInvalid_thenReturnsStatus400() throws Exception {
        LoginRequest input = invalidLogin();
        String body = objectMapper.writeValueAsString(input);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("password: size must be between 6 and 12")));

    }

    @Test
    void whenUsernameNotFound_thenThrowException() throws Exception {
        loginSetup();

        when(userService.findUserByUsernameAndPassword(request.getUsername(), request.getPassword()))
                .thenReturn(Optional.empty());

        when(mapperUtil.mapToUser(request)).thenReturn(user);

        mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void registerShouldReturnUser() throws Exception {
        registerSetup();

        when(userService.findUserByUsername(registerUserRequest.getUsername()))
                .thenReturn(Optional.empty());
        when(userService.registerUser(any())).thenReturn(user);

        when(mapperUtil.mapToUser(registerUserRequest)).thenReturn(user);
        when(mapperUtil.mapToUserResponse(user)).thenReturn(userResponse);

        mockMvc.perform(post("/sign-up")
                .content(objectMapper.writeValueAsBytes(registerUserRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userResponse.getUsername()))
                .andExpect(jsonPath("$.firstName").value(userResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userResponse.getLastName()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void whenRegisterInputIsInvalid_thenReturnsStatus400() throws Exception {
        RegisterUserRequest input = invalidRegister();
        String body = objectMapper.writeValueAsString(input);

        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());

    }

    @Test
    void whenUsernameAlreadyExists_thenThrowException() throws Exception {
        registerSetup();

        when(userService.findUserByUsername(registerUserRequest.getUsername()))
                .thenReturn(Optional.of(user));

        mockMvc.perform(post("/sign-up")
                .content(objectMapper.writeValueAsBytes(registerUserRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
