package com.hurriyet.todoapp.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {

    @NotNull
    @Size(min = 3, max = 12)
    @ApiModelProperty(value = "username of the user", example = "ramo")
    private String username;

    @NotNull
    @Size(min = 6, max = 12)
    @ApiModelProperty(value = "password of the user", example = "ramo123")
    private String password;
}
