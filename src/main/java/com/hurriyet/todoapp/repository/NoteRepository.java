package com.hurriyet.todoapp.repository;

import com.hurriyet.todoapp.model.data.Note;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteRepository extends CrudRepository<Note, String> {

    List<Note> findByUserId(String userId);

}
