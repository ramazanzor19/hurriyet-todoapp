package com.hurriyet.todoapp.cache;

import com.hurriyet.todoapp.model.data.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserCache {

    private final Cache userCache;

    public UserCache(Cache userCache) {
        this.userCache = userCache;
    }

    public User getUserFromCache(String username) {
        Cache.ValueWrapper element = userCache.get(username);
        if (element == null) {
            return null;
        } else {
            log.info("user cache hit -> username={}", username);
            return (User) element.get();
        }
    }

    public void putUserInCache(User user) {
        userCache.put(user.getUsername(), user);

    }

    public void removeUserFromCache(String username) {
        userCache.evict(username.toLowerCase());
    }
}
