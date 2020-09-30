package com.hurriyet.todoapp.service;

import com.hurriyet.todoapp.cache.UserCache;
import com.hurriyet.todoapp.model.data.User;
import com.hurriyet.todoapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    UserCache userCache;

    @InjectMocks
    UserService userService;


    @Test
    void shouldRegisterUser() {
        User user = new User(null, "user", "Ramazan", "Zor", "ramazan","password");
        User registered = new User("user::hash", "user", "Ramazan", "Zor", "ramazan","password");

        given(userRepository.save(user)).willReturn(registered);

        User savedUser = userService.registerUser(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(any(String.class));

    }

    @Test
    void shouldReturnUserFromCache() {
        User user = new User("user::hash", "user", "Ramazan", "Zor", "ramazan","password");
        String username = "ramazan";
        when(userCache.getUserFromCache(username)).thenReturn(user);

        Optional<User> userOpt = userService.findUserByUsername(username);

        verify(userCache).getUserFromCache(username);
        verify(userRepository, never()).findByUsername(username);
        verify(userCache, never()).putUserInCache(any(User.class));
        assertEquals(user, userOpt.get());

    }

    @Test
    void shouldReturnUserFromDb() {
        User user = new User("user::hash", "user", "Ramazan", "Zor", "ramazan","password");
        String username = "ramazan";
        when(userCache.getUserFromCache(username)).thenReturn(null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> userOpt = userService.findUserByUsername(username);

        verify(userCache).getUserFromCache(username);
        verify(userRepository).findByUsername(username);
        verify(userCache).putUserInCache(any(User.class));
        assertEquals(user, userOpt.get());

    }

    @Test
    void whenPasswordMatch_thenReturnUser() {

        User user = new User("user::hash", "user", "Ramazan", "Zor", "ramazan","password");
        String username = "ramazan";
        String password = "password";

        when(userService.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        Optional<User> userOpt = userService.findUserByUsernameAndPassword(username, password);

        verify(passwordEncoder).matches(password, user.getPassword());

        assertEquals(user, userOpt.get());
    }

    @Test
    void whenPasswordNotMatched_thenReturnEmpty() {

        User user = new User("user::hash", "user", "Ramazan", "Zor", "ramazan","password");
        String username = "ramazan";
        String password = "password";

        when(userService.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        Optional<User> userOpt = userService.findUserByUsernameAndPassword(username, password);

        verify(passwordEncoder).matches(password, user.getPassword());

        assertEquals(Optional.empty(), userOpt);
    }
}
