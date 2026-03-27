package com.taskmanager.controllers;

import com.taskmanager.dtos.requests.CreateTaskRequest;
import com.taskmanager.dtos.requests.UpdateTaskRequest;
import com.taskmanager.dtos.responses.ApiResponse;
import com.taskmanager.dtos.responses.TaskResponse;
import com.taskmanager.exceptions.TaskAlreadyExistsException;
import com.taskmanager.exceptions.TaskDoesNotExistException;
import com.taskmanager.services.TaskManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskManagerController {

    private final TaskManagerService taskManagerService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createTask(@RequestBody CreateTaskRequest request) {
        try {
            TaskResponse response = taskManagerService.createTask(request);
            return ResponseEntity.ok(new ApiResponse("Task created successfully", true, response));
        } catch (TaskAlreadyExistsException ex) {
            return ResponseEntity.status(409).body(new ApiResponse(ex.getMessage(), false, null));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse("Something went wrong" + ex.getMessage(), false, null));
        }
    }

    @PatchMapping("/completed/{id}")
    public ResponseEntity<ApiResponse> markTaskAsCompleted(@PathVariable String id) {
        try {
            TaskResponse response = taskManagerService.markTaskAsCompleted(id);
            return ResponseEntity.ok(new ApiResponse("Task marked successfully", true, response));
        } catch (TaskAlreadyExistsException ex) {
            return ResponseEntity.status(409).body(new ApiResponse(ex.getMessage(), false, null));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse("Something went wrong" + ex.getMessage(), false, null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTasks() {
        try {
            List<TaskResponse> responses = taskManagerService.getAllTasks();
            return ResponseEntity.ok(new ApiResponse("Tasks found", true, responses));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new ApiResponse("Something went wrong" + ex.getMessage(), false, null));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateTask(@PathVariable String id, @RequestBody UpdateTaskRequest request) {
        try {
            TaskResponse response = taskManagerService.updateTask(id, request);
            return ResponseEntity.ok(new ApiResponse("Task updated successfully", true, response));
        } catch (TaskAlreadyExistsException ex) {
            return ResponseEntity.status(409).body(new ApiResponse(ex.getMessage(), false, null));
        } catch (Exception ex) {
            return ResponseEntity.ok(new ApiResponse("Something went wrong" + ex.getMessage(), false, null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable String id) {
        try {
            taskManagerService.deleteTask(id);
            return ResponseEntity.ok(new ApiResponse("Task deleted successfully", true, null));
        } catch (TaskDoesNotExistException ex) {
            return ResponseEntity.status(404).body(new ApiResponse(ex.getMessage(), false, null));
        } catch (Exception ex) {
            return ResponseEntity.ok(new ApiResponse("Something went wrong" + ex.getMessage(), false, null));
        }
    }
}
