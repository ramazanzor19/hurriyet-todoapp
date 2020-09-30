package com.hurriyet.todoapp.util;

import com.hurriyet.todoapp.model.data.Note;
import com.hurriyet.todoapp.model.data.User;
import com.hurriyet.todoapp.model.request.LoginRequest;
import com.hurriyet.todoapp.model.request.NoteRequest;
import com.hurriyet.todoapp.model.request.RegisterUserRequest;
import com.hurriyet.todoapp.model.response.NoteResponse;
import com.hurriyet.todoapp.model.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MapperUtil {

    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User mapToUser(RegisterUserRequest user) {
        return this.modelMapper.map(user, User.class);
    }

    public User mapToUser(LoginRequest user) {
        return this.modelMapper.map(user, User.class);
    }

    public UserResponse mapToUserResponse(User user) {
        return this.modelMapper.map(user, UserResponse.class);
    }

    public Note mapToNote(NoteRequest noteRequest) {
        return this.modelMapper.map(noteRequest, Note.class);
    }

    public NoteResponse mapToNoteResponse(Note note) {
        return this.modelMapper.map(note, NoteResponse.class);
    }
}
