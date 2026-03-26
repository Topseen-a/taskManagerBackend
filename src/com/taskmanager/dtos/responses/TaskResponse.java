package com.taskmanager.dtos.responses;

import lombok.Data;

@Data
public class TaskResponse {

    private String id;
    private String title;
    private String description;
    private boolean completed;
    private String createdAt;
}
