package com.hurriyet.todoapp.controller;

import com.hurriyet.todoapp.model.request.Id;
import com.hurriyet.todoapp.model.data.Note;
import com.hurriyet.todoapp.model.request.NoteRequest;
import com.hurriyet.todoapp.model.response.NoteResponse;
import com.hurriyet.todoapp.service.NoteService;
import com.hurriyet.todoapp.util.MapperUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final  NoteService noteService;
    private final MapperUtil mapperUtil;

    public NoteController(NoteService noteService, MapperUtil mapperUtil) {
        this.noteService = noteService;
        this.mapperUtil = mapperUtil;
    }


    @GetMapping("/user-notes")
    public ResponseEntity<List<NoteResponse>> getUserNotes(Principal principal) {
        List<Note> notes =  noteService.getUserNotes(principal.getName());
        List<NoteResponse> noteResponses = notes.stream()
                .map(mapperUtil::mapToNoteResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(noteResponses);
    }

    @PostMapping("/save")
    public ResponseEntity<NoteResponse> saveUserNote(@Valid @RequestBody NoteRequest noteRequest, Principal principal) {

        Note note = mapperUtil.mapToNote(noteRequest);
        note.setUserId(principal.getName());
        Note saved = noteService.saveUserNote(note);
        return ResponseEntity.ok(mapperUtil.mapToNoteResponse(saved));
    }

    @PostMapping("/delete")
    public ResponseEntity<Id> deleteNote(@Valid @RequestBody Id id, Principal principal) {
        Optional<Note> noteOpt = noteService.getUserNote(id.getId());
        return noteOpt.filter(note -> note.getUserId().equals(principal.getName()))
                .map(note -> {
                    noteService.deleteUserNote(id.getId());
                    return ResponseEntity.ok(id);
                }).orElseGet(() ->  ResponseEntity.notFound().build());

    }
}
