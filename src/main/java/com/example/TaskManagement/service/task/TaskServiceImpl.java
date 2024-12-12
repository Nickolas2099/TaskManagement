package com.example.TaskManagement.service.task;

import com.example.TaskManagement.domain.api.task.TaskSaveReq;
import com.example.TaskManagement.domain.constant.Code;
import com.example.TaskManagement.domain.entity.Priority;
import com.example.TaskManagement.domain.entity.Status;
import com.example.TaskManagement.domain.entity.Task;
import com.example.TaskManagement.domain.entity.User;
import com.example.TaskManagement.domain.response.Response;
import com.example.TaskManagement.domain.response.SuccessResponse;
import com.example.TaskManagement.domain.response.error.Error;
import com.example.TaskManagement.domain.response.error.ErrorResponse;
import com.example.TaskManagement.domain.response.exception.CommonException;
import com.example.TaskManagement.repository.PriorityRepository;
import com.example.TaskManagement.repository.StatusRepository;
import com.example.TaskManagement.repository.TaskRepository;
import com.example.TaskManagement.repository.UserRepository;
import com.example.TaskManagement.service.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskServiceImpl implements TaskService {

    JwtService jwtService;
    TaskRepository taskRepository;
    UserRepository userRepository;
    StatusRepository statusRepository;
    PriorityRepository priorityRepository;

    @Override
    public ResponseEntity<Response> getTasksByAuthor(String authorEmail, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        User author = userRepository.findByEmail(authorEmail).orElseThrow(
                () -> CommonException.builder()
                        .code(Code.NOT_FOUND)
                        .techMessage("User with transmitted email haven't found")
                        .userMessage("Пользователь не найден")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()
        );
        Page<Task> tasks = taskRepository.getTasksByCreatedBy(author, pageable);
        return new ResponseEntity<>(SuccessResponse.builder().data(tasks).build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response> getTasksByAssignment(String executorEmail, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        User executor = userRepository.findByEmail(executorEmail).orElseThrow(
                () -> CommonException.builder()
                        .code(Code.NOT_FOUND)
                        .techMessage("User with transmitted email haven't found")
                        .userMessage("Пользователь не найден")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build()
        );
        Page<Task> tasks = taskRepository.getTasksByAssignedTo(executor, pageable);
        return new ResponseEntity<>(SuccessResponse.builder().data(tasks).build(), HttpStatus.OK);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseEntity<Response> editTask(String heading, Task task) { //TODO: заменить Entity на DTO

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email).get();
        boolean isAdmin = jwtService.isAdmin();

        if(isAdmin || user.equals(task.getCreatedBy())) {

            Task origTask = taskRepository.getTaskByHeading(heading).orElseThrow(
                    () -> new EntityNotFoundException("Task not found with heading: " + heading)
            );
            updateTaskFields(origTask, task, isAdmin);
            return new ResponseEntity<>(SuccessResponse.builder().data(origTask).build(), HttpStatus.OK);

        } else {

            return new ResponseEntity<>(ErrorResponse.builder().error(Error
                    .builder()
                    .code(Code.FORBIDDEN)
                    .techMessage("There are not enough permissions to change this task")
                    .build()
            ).build(), HttpStatus.FORBIDDEN);
        }
    }

    private void updateTaskFields(Task origTask, Task task, boolean isAdmin) {
        if (task.getHeading() != null) {
            origTask.setHeading(task.getHeading());
        }
        if (task.getDescription() != null) {
            origTask.setDescription(task.getDescription());
        }
        if (task.getStatus() != null) {
            origTask.setStatus(task.getStatus());
        }
        if (task.getPriority() != null) {
            origTask.setPriority(task.getPriority());
        }
        if (task.getAssignedTo() != null && isAdmin) {
            origTask.setAssignedTo(task.getAssignedTo());
        }
    }

    @Override
    public ResponseEntity<Response> saveTask(TaskSaveReq task) {

        taskRepository.save(task.getHeading(), task.getDescription(),
                task.getStatusId(), task.getPriorityId(),
                task.getAuthorId(), task.getExecutorId());
        return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Response> changePriority(String heading, String priorityTitle) {

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email).get();
        Task task = taskRepository.getTaskByHeading(heading).orElseThrow(
                () -> new EntityNotFoundException("Task haven't found with heading: " + heading)
        );

        if(jwtService.isAdmin() || task.getCreatedBy().equals(user)) {

            Priority priority = priorityRepository.getPrioritiesByTitle(priorityTitle).orElseThrow(
                    () -> new EntityNotFoundException("Priority haven't found with title: " + priorityTitle)
            );
            if(task.getPriority().equals(priority)) {

                return new ResponseEntity<>(ErrorResponse.builder().error(Error
                        .builder()
                        .code(Code.BAD_REQUEST)
                        .techMessage("There is the same priority have sent")
                        .build()
                ).build(), HttpStatus.BAD_REQUEST);
            } else {

                task.setPriority(priority);
                taskRepository.save(task);
                return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.OK);
            }
        } else {

            return new ResponseEntity<>(ErrorResponse.builder().error(Error
                    .builder()
                    .code(Code.FORBIDDEN)
                    .techMessage("There are not enough permissions to change priority of this task")
                    .build()
            ).build(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<Response> changeStatus(String heading, String statusTitle) {

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email).get();
        Task task = taskRepository.getTaskByHeading(heading).orElseThrow(
                () -> new EntityNotFoundException("Task haven't found with heading: " + heading)
        );

        if(jwtService.isAdmin() || task.getCreatedBy().equals(user)) {

            Status status = statusRepository.getStatusByTitle(statusTitle).orElseThrow(
                    () -> new EntityNotFoundException("Status haven't found with title: " + statusTitle)
            );
            if(task.getStatus().equals(status)) {

                return new ResponseEntity<>(ErrorResponse.builder().error(Error
                        .builder()
                        .code(Code.BAD_REQUEST)
                        .techMessage("There is the same status have sent")
                        .build()
                ).build(), HttpStatus.BAD_REQUEST);
            } else {

                task.setStatus(status);
                taskRepository.save(task);
                return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.OK);
            }
        } else {

            return new ResponseEntity<>(ErrorResponse.builder().error(Error
                    .builder()
                    .code(Code.FORBIDDEN)
                    .techMessage("There are not enough permissions to change status of this task")
                    .build()
            ).build(), HttpStatus.FORBIDDEN);
        }
    }
}
