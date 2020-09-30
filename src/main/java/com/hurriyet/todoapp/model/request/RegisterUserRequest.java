package com.hurriyet.todoapp.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterUserRequest {

    @NotNull
    @Size(min = 3, max = 36)
    @ApiModelProperty(value = "name of the user", example = "Ramazan")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 36)
    @ApiModelProperty(value = "surname of the user", example = "Zor")
    private String lastName;

    @NotNull
    @Size(min = 3, max = 12)
    @ApiModelProperty(value = "username of the user", example = "ramo")
    private String username;

    @NotNull
    @Size(min = 6, max = 12)
    @ApiModelProperty(value = "password of the user", example = "ramo123")
    private String password;
}
