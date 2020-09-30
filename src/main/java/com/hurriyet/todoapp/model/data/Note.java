package com.hurriyet.todoapp.model.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id @GeneratedValue(strategy = UNIQUE, delimiter = "::")
    private String id;
    @IdPrefix(order=0)
    private String prefix = "note";

    @Field
    private String text;

    @Field
    private String userId;

}
