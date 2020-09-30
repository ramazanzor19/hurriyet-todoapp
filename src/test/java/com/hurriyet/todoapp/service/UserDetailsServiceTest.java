package com.hurriyet.todoapp.service;

import com.hurriyet.todoapp.model.data.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldReturnUser() {
        String username = "ramazan";
        User user = new User("user::hash", "user", "Ramazan", "Zor", "ramazan","password");

        when(userService.findUserByUsername(username)).thenReturn(Optional.of(user));

        User userLoaded = userDetailsService.loadUserByUsername(username);

        assertEquals(userLoaded, user);
    }

    @Test
    void shouldThrowUserNotFound() {
        String username = "ramazan";

        when(userService.findUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });

    }
}
