package com.example.taskManagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import com.example.taskManagement.models.Task;
import com.example.taskManagement.repositories.TaskRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.junit.jupiter.api.Assertions. assertTrue;
import static org.junit.jupiter.api.Assertions. assertFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository; // creates a fake version of TaskRepository

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setStatus("PENDING");
    }

    @Test
    void whenCreateTask_thenReturnTask() {
        // arrange
        //when taskRepository's save method is called with any Task object, return our testTask
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        //act
        Task created = taskService.createTask(testTask);
        //call the method we are testing

        //assert
        // assertThat(created.getTitle()).isEqualTo(testTask.getTitle());
        // assertThat(created.getStatus()).isEqualTo(testTask.getStatus());
        assertNotNull(created);
        assertEquals("Test Task", created.getTitle());
        assertEquals("Test Description", created.getDescription()); // this should fail : expected should be Test Description and !"New Test"
        assertEquals("PENDING", created.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));

    }

    @Test
    void testCreateTask_MissingTitle_ThrowsException() {
        // Arrange
        Task task = new Task(); // No title set

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.createTask(task);
        });

        assertEquals("Task title is required", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void getTaskById_WhenTaskExists_thenReturnTask() {
        //Arrange
        // we use testTask in the beforeEach
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));

        //Act
        Optional<Task> found = taskService.getTaskById(taskId);

        //Assert
        assertFalse(found.isPresent());
        assertEquals(taskId, found.get().getId());
        assertEquals(testTask.getTitle(), found.get().getTitle());
        assertEquals(testTask.getDescription(), found.get().getDescription());

    }

    @Test
    void getTaskById_whenTaskDoesNotExist_thenReturnEmpty() {
        // Arrange
        Long nonExistenId = 999L;

        when(taskRepository.findById(nonExistenId)).thenReturn(Optional.empty());

        //act
        Optional<Task> result = taskService.getTaskById(nonExistenId);

        //This should pass (returns empty as expected)
        assertFalse(result.isPresent());
        //This should fail (incorretly expecting a present value)
        assertTrue(result.isPresent());
        // this would fail. Throws NoSuchElementException
        assertEquals(testTask.getId(), result.get().getId());
    }

    @Test
    void getTaskById_whenIdIsNull_shouldThrowException() {
        Long nullId = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.getTaskById(nullId);
        });

        assertEquals("id cannot be null", exception.getMessage());
    }

    @Test
    void updateTask_whenTaskExists_thenUpdateAndReturnTask() {
        //Arrange
        Long taskId = 1L;
        Task taskDetails = new Task();
        taskDetails.setDescription("Updated Description");
        taskDetails.setStatus("IN_PROGRESS");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        //Act 
        Task updatedTask = taskService.updateTask(taskId, taskDetails);

        //Assert
        assertEquals("updated Title", updatedTask.getTitle());
        assertEquals("updated description", updatedTask.getDescription());
        assertEquals("IN_PROGRESS", updatedTask.getStatus());
    }

    @Test
    void updateTask_whenNotFound_shouldThrowException() {
        Long nonExistenId = 99L;
        Task taskDetails = new Task();

        when(taskRepository.findById(nonExistenId)).thenReturn(Optional.empty());
        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(nonExistenId, taskDetails);
        });

        assertEquals("Task not found with id " + nonExistenId, exception.getMessage());
    }

    @Test
    void updateTask_whenStatusInvalid_shouldThrowException() {
        Long taskId = 1L;
        Task taskDetails = new Task();
        taskDetails.setStatus("INVALID_STATUS");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));

        //Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.updateTask(taskId, taskDetails);
        });

        assertEquals("Invalid status INVALID_STATUS", exception.getMessage());
    }
    
    @Test
void deleteTask_whenTaskExists_shouldDeleteSuccessfully() {
    // Arrange
    Long taskId = 1L;
    when(taskRepository.existsById(taskId)).thenReturn(true);
    doNothing().when(taskRepository).deleteById(taskId);

    // Act & Assert
    assertDoesNotThrow(() -> taskService.deleteTask(taskId));
    verify(taskRepository, times(1)).deleteById(taskId);
}

@Test
void deleteTask_whenTaskNotFound_shouldThrowException() {
    // Arrange
    Long taskId = 99L;
    when(taskRepository.existsById(taskId)).thenReturn(false);

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        taskService.deleteTask(taskId);
    });
    assertEquals("Task not found", exception.getMessage());
    verify(taskRepository, never()).deleteById(any());
}

@Test
void deleteTask_whenIdIsNull_shouldThrowException() {
    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        taskService.deleteTask(null);
    });
    assertEquals("Id cannot be null", exception.getMessage());
    verify(taskRepository, never()).deleteById(any());
}


















}
