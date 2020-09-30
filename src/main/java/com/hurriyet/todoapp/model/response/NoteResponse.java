package com.hurriyet.todoapp.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteResponse {

    @ApiModelProperty(value = "note id", example = "note::hash")
    private String id;

    @ApiModelProperty(value = "note information", example = "eklenen not")
    private String text;

    @ApiModelProperty(value = "note user id", example = "ramo")
    private String userId;
}
