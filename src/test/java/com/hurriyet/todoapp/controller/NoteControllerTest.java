package com.hurriyet.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hurriyet.todoapp.model.data.Note;
import com.hurriyet.todoapp.model.request.Id;
import com.hurriyet.todoapp.model.request.NoteRequest;
import com.hurriyet.todoapp.model.response.NoteResponse;
import com.hurriyet.todoapp.service.NoteService;
import com.hurriyet.todoapp.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private NoteService noteService;

    @MockBean
    private MapperUtil mapperUtil;

    @Autowired
    private MockMvc mockMvc;

    List<Note> notes = Arrays.asList(
      new Note("note::hash","note","text1","ramazan"),
      new Note("note::hash","note","text2","ramazan")
    );

    List<NoteResponse> noteResponses = Arrays.asList(
            new NoteResponse("note::hash","text1","ramazan"),
            new NoteResponse("note::hash","text2","ramazan")
    );

    private Id id;
    private NoteRequest request;
    private NoteResponse response;
    private Note note;

    @BeforeTestMethod
    void saveSetup() {
        Id id = new Id("note:hash");
        this.id = id;
        NoteRequest request = new NoteRequest("text1");
        this.request = request;

        NoteResponse response = new NoteResponse("note::hash", "text1", "ramazan");
        this.response = response;

        Note note = new Note("note::hash","note", "text1", "ramazan");
        this.note = note;
    }

    @Test
    @WithMockUser(username="ramazan")
    void shouldReturnUserNotes() throws Exception {

        when(noteService.getUserNotes("ramazan"))
                .thenReturn(notes);

        when(mapperUtil.mapToNoteResponse(notes.get(0)))
                .thenReturn(noteResponses.get(0));
        when(mapperUtil.mapToNoteResponse(notes.get(1)))
                .thenReturn(noteResponses.get(1));

        mockMvc.perform(get("/notes/user-notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text", is("text1")));
    }

    @Test
    void whenUserNotExists_thenGetUserNotesReturnNotAuthorized() throws Exception {
        mockMvc.perform(get("/notes/user-notes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="ramazan")
    void saveNoteShouldReturnUserNote() throws Exception {
        saveSetup();

        when(noteService.saveUserNote(any())).thenReturn(note);
        when(mapperUtil.mapToNote(any())).thenReturn(note);
        when(mapperUtil.mapToNoteResponse(any())).thenReturn(response);

        mockMvc.perform(post("/notes/save")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.text").value(response.getText()))
                .andExpect(jsonPath("$.userId").value(response.getUserId()));

    }

    @Test
    @WithMockUser(username="ramazan")
    void whenSaveInputIsInvalid_thenReturnsStatus400() throws Exception {

        NoteRequest noteRequest = new NoteRequest("text");

        mockMvc.perform(post("/notes/save")
                .content(objectMapper.writeValueAsBytes(noteRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem("text: size must be between 5 and 512")));

    }

    @Test
    void whenUserNotExists_thenSaveNoteReturnNotAuthorized() throws Exception {
        saveSetup();
        mockMvc.perform(post("/notes/save")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenUserNotExists_thenDeleteNoteReturnNotAuthorized() throws Exception {
        saveSetup();
        mockMvc.perform(post("/notes/delete")
                .content(objectMapper.writeValueAsBytes(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="ramazan")
    void deleteNoteShouldReturnId() throws Exception {
        saveSetup();

        when(noteService.getUserNote(id.getId())).thenReturn(Optional.of(note));

        mockMvc.perform(post("/notes/delete")
                .content(objectMapper.writeValueAsBytes(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="ramazan")
    void whenIdNotExists_thenReturnNotFound() throws Exception {
        saveSetup();

        when(noteService.getUserNote(id.getId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/notes/delete")
                .content(objectMapper.writeValueAsBytes(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="ramazan2")
    void whenUserIdNotMatched_thenReturnNotFound() throws Exception {
        saveSetup();

        when(noteService.getUserNote(id.getId())).thenReturn(Optional.of(note));

        mockMvc.perform(post("/notes/delete")
                .content(objectMapper.writeValueAsBytes(id))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
