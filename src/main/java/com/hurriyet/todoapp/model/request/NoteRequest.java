package com.hurriyet.todoapp.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequest {

    @NotNull
    @Size(min = 5, max = 512)
    @ApiModelProperty(value = "note information", example = "yeni not ekleyelim")
    private String text;
}
