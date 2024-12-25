package com.example.taskManagement.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskManagement.models.Task;
import com.example.taskManagement.repositories.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task createTask(Task task) {
        
        if (task.getTitle() == null || task.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("Task title is required");

        task.setStatus("PENDING");
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id cannot be null");
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> getAllTasks() {
    
        return taskRepository.findAll();
    }

    @Override
    public Task updateTask(Long id, Task taskDetails) {
        Task existingTask = taskRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Task not found with id " + id));
        if(taskDetails.getTitle() != null && !taskDetails.getTitle().trim().isEmpty())
            existingTask.setTitle(taskDetails.getTitle());

        if(taskDetails.getDescription() != null)
            existingTask.setDescription(taskDetails.getDescription());
        
        if (taskDetails.getStatus() != null) {
            validateStatus(taskDetails.getStatus());
            existingTask.setStatus(taskDetails.getStatus());
        }

        existingTask.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(existingTask);
 
    }

    @Override
    public void deleteTask(Long id) {
        if(!taskRepository.existsById(id))
            throw new RuntimeException("Task not found");

        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> getTasksByStatus(String status) {
        validateStatus(status);
        return taskRepository.findByStatus(status);
    }

    @Override
    public Task markTaskAsComplete(Long id) {
        // TODO Auto-generated method stub   -> version 2.0
        throw new UnsupportedOperationException("Unimplemented method 'markTaskAsComplete'");
    }

    @Override
    public List<Task> searchTasks(String keyword) {
       if(keyword == null || keyword.trim().isEmpty())
           throw new IllegalArgumentException("Search keyword cannot be empty");
       return taskRepository.findByTitleContainingIgnoreCase(keyword.trim());
    }

    @Override
    public List<Task> getRecentTasks(int days) {
        // TODO Auto-generated method stub   -> version 2.0
        throw new UnsupportedOperationException("Unimplemented method 'getRecentTasks'");
    }

    //helper method for validation 
    private void validateStatus(String status) {
        List<String> validStatus = List.of("PENDING", "IN_PROCESS", "COMPLETED");
        if(!validStatus.contains(status))
            throw new IllegalArgumentException("Invalid status " + status);

    }
}
