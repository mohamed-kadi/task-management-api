package com.example.taskManagement.services;

import java.util.List;
import java.util.Optional;

import com.example.taskManagement.models.Task;

public interface TaskService {

    Task createTask(Task task);

    Optional<Task> getTaskById(Long id);

    List<Task> getAllTasks();

    Task updateTask(Long id, Task task);

    void deleteTask(Long id); // we can have Task as return type, it will return the deleted task

    // Business-specific operations
    List<Task> getTasksByStatus(String Status);

    Task markTaskAsComplete(Long id);  // -> version 2.0

    List<Task> searchTasks(String keyword);

    List<Task> getRecentTasks(int days);  // -> version 2.0

}
