package com.taskmanager.dtos.requests;

import lombok.Data;

@Data
public class CreateTaskRequest {

    private String title;
    private String description;
}
