package com.hurriyet.todoapp.service;

import com.hurriyet.todoapp.cache.UserCache;
import com.hurriyet.todoapp.model.data.User;
import com.hurriyet.todoapp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserCache userCache;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserCache userCache, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userCache = userCache;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public Optional<User> findUserByUsername(String username) {
        return Optional.ofNullable(userCache.getUserFromCache(username))
                .map(Optional::of)
                .orElseGet(() -> {
                    Optional<User> userFromRepo = userRepository.findByUsername(username);
                    userFromRepo.ifPresent(userCache::putUserInCache);
                    return userFromRepo;
                });
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {
        return findUserByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

}
