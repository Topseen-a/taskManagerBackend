package com.taskmanager.services;

import com.taskmanager.data.models.Task;
import com.taskmanager.data.repositories.TaskRepository;
import com.taskmanager.dtos.requests.CreateTaskRequest;
import com.taskmanager.dtos.requests.UpdateTaskRequest;
import com.taskmanager.dtos.responses.TaskResponse;
import com.taskmanager.exceptions.TaskAlreadyExistsException;
import com.taskmanager.exceptions.TaskDoesNotExistException;
import com.taskmanager.exceptions.TitleCannotBeBlankException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskManagerServiceTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskManagerService taskManagerService;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    public void testThatA_newTaskIsCreatedButHasNotBeenCompleted() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Java");
        request.setDescription("Strings, OOP, Layered Architecture");

        TaskResponse response = taskManagerService.createTask(request);

        assertEquals("Learn Java", response.getTitle());
        assertFalse(response.isCompleted());
    }

    @Test
    public void testThatA_taskCreatedCanBeMarkedAsCompleted() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Java");
        request.setDescription("Strings, OOP, Layered Architecture");

        TaskResponse response = taskManagerService.createTask(request);

        TaskResponse updatedTask = taskManagerService.markTaskAsCompleted(response.getId());

        assertTrue(updatedTask.isCompleted());
    }

    @Test
    public void testThatAnExistingTaskIsCreatedThrowsAnError() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Java");
        request.setDescription("Strings, OOP, Layered Architecture");
        taskManagerService.createTask(request);

        CreateTaskRequest duplicate = new CreateTaskRequest();
        duplicate.setTitle("Learn Java");
        duplicate.setDescription("Strings, OOP, Layered Architecture");

        assertThrows(TaskAlreadyExistsException.class, () -> taskManagerService.createTask(duplicate));
    }

    @Test
    public void testThatMultipleTasksCanBeCreated() {
        CreateTaskRequest requestOne = new CreateTaskRequest();
        requestOne.setTitle("Learn Java");
        requestOne.setDescription("Strings, OOP, Layered Architecture");
        taskManagerService.createTask(requestOne);

        CreateTaskRequest requestTwo = new CreateTaskRequest();
        requestTwo.setTitle("Learn Python");
        requestTwo.setDescription("Collections, Django, Files and Records");
        taskManagerService.createTask(requestTwo);

        List<Task> allTasks = taskRepository.findAll();
        assertEquals(2, allTasks.size());
    }

    @Test
    public void testThatTaskCreatedWithoutA_titleThrowsAnError() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("");
        request.setDescription("Strings, OOP, Layered Architecture");

        assertThrows(TitleCannotBeBlankException.class, () -> taskManagerService.createTask(request));
    }

    @Test
    public void testThatFindAllTasksReturnsAllTasks() {
        CreateTaskRequest requestOne = new CreateTaskRequest();
        requestOne.setTitle("Learn Java");
        requestOne.setDescription("Strings, OOP, Layered Architecture");
        taskManagerService.createTask(requestOne);

        CreateTaskRequest requestTwo = new CreateTaskRequest();
        requestTwo.setTitle("Learn Python");
        requestTwo.setDescription("Collections, Django, Files and Records");
        taskManagerService.createTask(requestTwo);

        List<TaskResponse> allTasks = taskManagerService.getAllTasks();
        assertEquals(2, allTasks.size());
    }

    @Test
    public void testThatTaskCanBeUpdated() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Java");
        request.setDescription("Strings, OOP, Layered Architecture");

        TaskResponse createdTask = taskManagerService.createTask(request);

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setTitle("Learn Java");
        updateTaskRequest.setDescription("Maven, Spring boot, Spring security");
        updateTaskRequest.setCompleted(true);

        TaskResponse updatedTaskResponse = taskManagerService.updateTask(createdTask.getId(), updateTaskRequest);

        assertEquals("Learn Java", updatedTaskResponse.getTitle());
        assertEquals("Maven, Spring boot, Spring security",  updatedTaskResponse.getDescription());
        assertTrue(updatedTaskResponse.isCompleted());
    }

    @Test
    public void testThatTaskCanBeDeleted() {
        CreateTaskRequest requestOne = new CreateTaskRequest();
        requestOne.setTitle("Learn Java");
        requestOne.setDescription("Strings, OOP, Layered Architecture");

        TaskResponse responseOne = taskManagerService.createTask(requestOne);

        CreateTaskRequest requestTwo = new CreateTaskRequest();
        requestTwo.setTitle("Learn Python");
        requestTwo.setDescription("Collections, Django, Files and Records");

        TaskResponse responseTwo = taskManagerService.createTask(requestTwo);

        taskManagerService.deleteTask(responseOne.getId());

        assertEquals(1, taskManagerService.getAllTasks().size());
    }

    @Test
    public void testThatDeletingA_taskThatDoesNotExistThrowsAnError() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Java");
        request.setDescription("Strings, OOP, Layered Architecture");

        TaskResponse response = taskManagerService.createTask(request);

        taskManagerService.deleteTask(response.getId());

        assertThrows(TaskDoesNotExistException.class, () -> taskManagerService.deleteTask(response.getId()));
    }
}
