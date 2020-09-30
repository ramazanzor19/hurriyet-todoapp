package com.hurriyet.todoapp.util;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

@Setter
@Getter
public class CachingProperties {
    int duration;
    TimeUnit timeUnit;
}
