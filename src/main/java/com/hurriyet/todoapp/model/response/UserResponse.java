package com.hurriyet.todoapp.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserResponse {

    @ApiModelProperty(value = "name of the user", example = "Ramazan")
    private String firstName;

    @ApiModelProperty(value = "surname of the user", example = "Zor")
    private String lastName;

    @ApiModelProperty(value = "username of the user", example = "ramo")
    private String username;
}
