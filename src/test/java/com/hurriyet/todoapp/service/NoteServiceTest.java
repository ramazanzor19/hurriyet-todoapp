package com.hurriyet.todoapp.service;

import com.hurriyet.todoapp.model.data.Note;
import com.hurriyet.todoapp.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    NoteRepository noteRepository;

    @InjectMocks
    NoteService noteService;

    @Test
    void shouldSaveUserNote() {
        Note note = new Note(null, "note", "textnote","ramazan");
        Note noteSaved = new Note("note::hash", "note", "textnote","ramazan");

        when(noteRepository.save(note)).thenReturn(noteSaved);

        Note noteS = noteService.saveUserNote(note);

        assertThat(noteS).isNotNull();

        verify(noteRepository).save(any(Note.class));

    }

    @Test
    void shouldGetUserNote() {
        String noteId = "note::hash";
        Note note = new Note("note::hash", "note", "textnote","ramazan");

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        Optional<Note> noteOpt = noteService.getUserNote(noteId);

        assertEquals(noteOpt, Optional.of(note));

        verify(noteRepository).findById(any(String.class));

    }

    @Test
    void shouldDeleteUserNote() {
        String noteId = "note::hash";

        noteService.deleteUserNote(noteId);

        verify(noteRepository).deleteById(any(String.class));

    }

    @Test
    void shouldGetUserNotes() {
        String userId = "ramazan";
        List<Note> notes = Arrays.asList(
                new Note("note::hash", "note", "textnote","ramazan"),
                new Note("note::hash2", "note", "textnote2","ramazan")
        );

        when(noteRepository.findByUserId(userId)).thenReturn(notes);

        List<Note> noteReturned = noteService.getUserNotes(userId);

        assertEquals(notes, noteReturned);

    }
}
