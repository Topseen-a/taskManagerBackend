package com.taskmanager.mapper;

import com.taskmanager.data.models.Task;
import com.taskmanager.dtos.requests.CreateTaskRequest;
import com.taskmanager.dtos.requests.UpdateTaskRequest;
import com.taskmanager.dtos.responses.TaskResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Mapper {

    public Task toEntity(CreateTaskRequest request) {
        Task todo = new Task();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(false);
        todo.setCreatedAt(LocalDateTime.now());

        return todo;
    }

    public TaskResponse toResponse(Task todo) {
        TaskResponse response = new TaskResponse();
        response.setId(todo.getId());
        response.setTitle(todo.getTitle());
        response.setDescription(todo.getDescription());
        response.setCompleted(todo.isCompleted());
        response.setCreatedAt(todo.getCreatedAt().toString());

        return response;
    }

    public void updateEntity(Task todo, UpdateTaskRequest request) {
        if (request.getTitle() != null) todo.setTitle(request.getTitle());
        if (request.getDescription() != null) todo.setDescription(request.getDescription());
        if (request.getCompleted() != null) todo.setCompleted(request.getCompleted());
    }
}
