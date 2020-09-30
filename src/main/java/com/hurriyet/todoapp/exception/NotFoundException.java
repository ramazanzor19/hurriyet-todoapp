package com.hurriyet.todoapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotFoundException extends Exception {

    private String message;
}
