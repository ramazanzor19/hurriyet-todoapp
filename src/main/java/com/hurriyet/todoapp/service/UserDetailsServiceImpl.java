package com.hurriyet.todoapp.service;

import com.hurriyet.todoapp.cache.UserCache;
import com.hurriyet.todoapp.model.data.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        return userService.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

    }

}
