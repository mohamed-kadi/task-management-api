package com.example.taskManagement.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.taskManagement.models.Task;
import com.example.taskManagement.services.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    /*  *** 1. Field injection is not recommanded but worth knowing ***
     * @Autowired 
     * private TaskService taskService
     * +++++++++++++++++++
     * *** 2. Setter injection ***
     * private TaskService taskService
     * @AutoWired 
     * public void setTaskService(TaskService taskService){
     * this.taskService = taskService;
     * }
     */

    private final TaskService taskService;

    /* *** 3. Constructor injection *** */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        /*  return taskService.getTaskById(id)
                 .map(ResponseEntity::ok)   //.map(task -> ResponseEntity.ok(task)   if task exists, wrap it in ResponseEntity
             .orElse(ResponseEntity.notFound().build());
        */
        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (taskOptional.isPresent())
            return ResponseEntity.ok(taskOptional.get());
        else
            return ResponseEntity.notFound().build();
    }
    
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String keyword) {
        List<Task> tasks = taskService.searchTasks(keyword);
        return ResponseEntity.ok(tasks);
    }


}
