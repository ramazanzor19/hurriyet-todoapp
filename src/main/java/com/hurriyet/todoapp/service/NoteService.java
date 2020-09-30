package com.hurriyet.todoapp.service;

import com.hurriyet.todoapp.model.data.Note;
import com.hurriyet.todoapp.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository repository;

    public NoteService(NoteRepository repository) {
        this.repository = repository;
    }

    public Note saveUserNote(Note note) {
        return repository.save(note);
    }

    public Optional<Note> getUserNote(String noteId) {
        return repository.findById(noteId);
    }

    public void deleteUserNote(String noteId) {
        repository.deleteById(noteId);
    }

    public List<Note> getUserNotes(String userId) {
        return repository.findByUserId(userId);
    }
}
