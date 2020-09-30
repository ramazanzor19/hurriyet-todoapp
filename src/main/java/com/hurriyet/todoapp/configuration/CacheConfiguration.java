package com.hurriyet.todoapp.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.hurriyet.todoapp.util.CachingProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@EnableCaching
@Configuration
@ConfigurationProperties(prefix = "cache")
public class CacheConfiguration {

    CachingProperties user;

    @Bean
    public CaffeineCache page() {
        return new CaffeineCache("user",
                Caffeine.newBuilder()
                        .expireAfterWrite(user.getDuration(), user.getTimeUnit())
                        .maximumSize(200)
                        .build());
    }
}
