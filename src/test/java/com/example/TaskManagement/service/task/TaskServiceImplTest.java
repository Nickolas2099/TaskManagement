package com.example.TaskManagement.service.task;

import com.example.TaskManagement.domain.entity.Priority;
import com.example.TaskManagement.domain.entity.Task;
import com.example.TaskManagement.domain.entity.User;
import com.example.TaskManagement.domain.response.Response;
import com.example.TaskManagement.domain.response.error.ErrorResponse;
import com.example.TaskManagement.repository.PriorityRepository;
import com.example.TaskManagement.repository.TaskRepository;
import com.example.TaskManagement.service.security.utility.UtilitySecurityService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private UtilitySecurityService securityService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private PriorityRepository priorityRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testChangePriority_Success() {

        String heading = "заголовок";
        String highPriorityTitle = "высокий";
        String lowPriorityTitle = "низкий";
        User user = new User();
        Task task = new Task();
        task.setCreatedBy(user);
        Priority highPriority = new Priority();
        Priority midPriority = new Priority();
        highPriority.setTitle(highPriorityTitle);
        midPriority.setTitle(lowPriorityTitle);
        task.setPriority(highPriority);

        when(securityService.getCurrentUser()).thenReturn(user);
        when(taskRepository.getTaskByHeading(heading)).thenReturn(Optional.of(task));
        when(priorityRepository.getPriorityByTitle(highPriorityTitle)).thenReturn(Optional.of(midPriority));
        when(securityService.isAdmin()).thenReturn(false);

        ResponseEntity<Response> response = taskService.changePriority(heading, highPriorityTitle);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testChangePriority_TaskNotFound() {

        String heading = "несуществующий заголовок";
        String priorityTitle = "высокий";

        when(taskRepository.getTaskByHeading(heading)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            taskService.changePriority(heading, priorityTitle);
        });

        assertEquals("Task haven't found with heading: " + heading, exception.getMessage());
    }

    @Test
    void testChangePriority_PriorityNotFound() {

        String heading = "заголовок";
        String priorityTitle = "приоритет";
        User user = new User();
        Task task = new Task();
        task.setCreatedBy(user);

        when(securityService.getCurrentUser()).thenReturn(user);
        when(taskRepository.getTaskByHeading(heading)).thenReturn(Optional.of(task));
        when(priorityRepository.getPriorityByTitle(priorityTitle)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            taskService.changePriority(heading, priorityTitle);
        });

        assertEquals("Priority haven't found with title: " + priorityTitle, exception.getMessage());
    }

    @Test
    void testChangePriority_SamePriority() {

        String heading = "заголовок";
        String priorityTitle = "высокий";
        User user = new User();
        Task task = new Task();
        task.setCreatedBy(user);
        Priority priority = new Priority();
        priority.setTitle(priorityTitle);
        task.setPriority(priority);

        when(securityService.getCurrentUser()).thenReturn(user);
        when(taskRepository.getTaskByHeading(heading)).thenReturn(Optional.of(task));
        when(priorityRepository.getPriorityByTitle(priorityTitle)).thenReturn(Optional.of(priority));
        when(securityService.isAdmin()).thenReturn(false);

        ResponseEntity<Response> response = taskService.changePriority(heading, priorityTitle);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("There is the same priority have sent",
                ((ErrorResponse) response.getBody()).getError().getTechMessage());
    }

    @Test
    void testChangePriority_Forbidden() {

        String heading = "заголовок";
        String priorityTitle = "высокий";
        User currentUser = new User();
        currentUser.setId(1);
        User author = new User();
        author.setId(2);
        Task task = new Task();
        task.setCreatedBy(author);

        when(securityService.getCurrentUser()).thenReturn(currentUser);
        when(taskRepository.getTaskByHeading(heading)).thenReturn(Optional.of(task));
        when(securityService.isAdmin()).thenReturn(false);

        ResponseEntity<Response> response = taskService.changePriority(heading, priorityTitle);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("There are not enough permissions to change priority of this task",
                ((ErrorResponse) response.getBody()).getError().getTechMessage());
    }

}
