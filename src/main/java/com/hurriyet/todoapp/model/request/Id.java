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
public class Id {

    @NotNull
    @Size(min = 5)
    @ApiModelProperty(value = "id of the data", example = "type::hash")
    private String id;
}
