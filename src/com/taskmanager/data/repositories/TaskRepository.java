package com.taskmanager.data.repositories;

import com.taskmanager.data.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

    boolean  existsByTitle(String title);
}
