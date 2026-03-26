package com.taskmanager.services;

import com.taskmanager.data.models.Task;
import com.taskmanager.data.repositories.TaskRepository;
import com.taskmanager.dtos.requests.CreateTaskRequest;
import com.taskmanager.dtos.responses.TaskResponse;
import com.taskmanager.exceptions.TaskAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TaskManagerServiceTest {

    @Autowired
    private TaskManagerService service;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    public void testThatA_newTaskIsCreated() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Java");
        request.setDescription("Strings, OOP, Layered Architecture");

        TaskResponse response = service.createTask(request);

        assertEquals("Learn Java", response.getTitle());
    }

    @Test
    public void testThatAnExistingTaskIsCreatedThrowsAnError() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Java");
        request.setDescription("Strings, OOP, Layered Architecture");
        service.createTask(request);

        CreateTaskRequest duplicate = new CreateTaskRequest();
        duplicate.setTitle("Learn Java");
        duplicate.setDescription("Strings, OOP, Layered Architecture");

        assertThrows(TaskAlreadyExistsException.class, () -> service.createTask(duplicate));
    }

    @Test
    public void testThatMultipleTasksCanBeCreated() {
        CreateTaskRequest requestOne = new CreateTaskRequest();
        requestOne.setTitle("Learn Java");
        requestOne.setDescription("Strings, OOP, Layered Architecture");
        service.createTask(requestOne);

        CreateTaskRequest requestTwo = new CreateTaskRequest();
        requestTwo.setTitle("Learn Python");
        requestTwo.setDescription("Collections, Django, Files and Records");
        service.createTask(requestTwo);

        List<Task> allTasks = taskRepository.findAll();
        assertEquals(2, allTasks.size());
    }
}
