package com.taskmanager.services;

import com.taskmanager.data.models.Task;
import com.taskmanager.data.repositories.TaskRepository;
import com.taskmanager.dtos.requests.CreateTaskRequest;
import com.taskmanager.dtos.requests.UpdateTaskRequest;
import com.taskmanager.dtos.responses.TaskResponse;
import com.taskmanager.exceptions.TaskAlreadyExistsException;
import com.taskmanager.exceptions.TaskDoesNotExistException;
import com.taskmanager.exceptions.TitleCannotBeBlankException;
import com.taskmanager.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskManagerService {

    private final TaskRepository taskRepository;
    private final Mapper mapper;

    public TaskResponse createTask(CreateTaskRequest request) {
        if (taskRepository.existsByTitle(request.getTitle())) throw new TaskAlreadyExistsException("Task already exists");
        if (request.getTitle().trim().isEmpty()) throw new TitleCannotBeBlankException("Title cannot be empty");

        Task todo = mapper.toEntity(request);
        Task savedTask = taskRepository.save(todo);

        return mapper.toResponse(savedTask);
    }

    public TaskResponse markTaskAsCompleted(String id) {
        Task todo = taskRepository.findById(id)
                .orElseThrow(() -> new TaskDoesNotExistException("Task does not exist"));

        todo.setCompleted(true);

        Task updatedTask = taskRepository.save(todo);
        return mapper.toResponse(updatedTask);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public TaskResponse updateTask(String id, UpdateTaskRequest request) {
        Task todo = taskRepository.findById(id)
                        .orElseThrow(() -> new TaskDoesNotExistException("Task does not exist"));

        mapper.updateEntity(todo, request);
        Task updatedTask = taskRepository.save(todo);

        return mapper.toResponse(updatedTask);
    }

    public void deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskDoesNotExistException("Task does not exist");
        }
        taskRepository.deleteById(id);
    }
}
