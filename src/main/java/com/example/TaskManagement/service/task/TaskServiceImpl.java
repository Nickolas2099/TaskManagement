package com.example.TaskManagement.service.task;

import com.example.TaskManagement.domain.api.task.TaskReq;
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
import com.example.TaskManagement.service.security.utility.UtilitySecurityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskServiceImpl implements TaskService {

    UtilitySecurityService securityService;
    TaskRepository taskRepository;
    UserRepository userRepository;
    StatusRepository statusRepository;
    PriorityRepository priorityRepository;

    @Override
    public ResponseEntity<Response> getByAuthor(String authorEmail, Integer page, Integer size) {

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
    public ResponseEntity<Response> getByAssignment(String executorEmail, Integer page, Integer size) {

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
    public ResponseEntity<Response> edit(Integer id, TaskReq task) {

        User user = securityService.getCurrentUser();
        Task origTask = taskRepository.getTaskById(id).orElseThrow(
                () -> new EntityNotFoundException("Task haven't found")
        );
        boolean isAdmin = securityService.isAdmin();

        if(isAdmin || user.equals(origTask.getCreatedBy())) {

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

    private void updateTaskFields(Task origTask, TaskReq task, boolean isAdmin) {
            origTask.setHeading(task.getHeading());
            origTask.setDescription(task.getDescription());
            origTask.setStatus(statusRepository.getStatusByTitle(task.getStatusTitle()).orElseThrow(
                    () -> new EntityNotFoundException("Status with id haven't found")
            ));
            origTask.setPriority(priorityRepository.getPriorityByTitle(task.getPriorityTitle()).orElseThrow(
                    () -> new EntityNotFoundException("Priority with id haven't found")
            ));
        if (isAdmin) {
            origTask.setAssignedTo(userRepository.findByEmail(task.getExecutorEmail()).orElseThrow(
                    () -> new EntityNotFoundException("Executor with id haven't found")
            ));
        }
    }

    @Override
    public ResponseEntity<Response> save(TaskReq req) {

        Status status = statusRepository.getStatusByTitle(req.getStatusTitle()).orElseThrow(
                () -> new EntityNotFoundException("status haven't found with title: " + req.getStatusTitle())
        );
        Priority priority = priorityRepository.getPriorityByTitle(req.getPriorityTitle()).orElseThrow(
                () -> new EntityNotFoundException("priority haven't found with title: " + req.getPriorityTitle())
        );
        User author = securityService.getCurrentUser();
        User executor = userRepository.findByEmail(req.getExecutorEmail()).orElseThrow(
                () -> new EntityNotFoundException("user haven't found with email: " + req.getExecutorEmail())
        );

        taskRepository.save(Task
                        .builder()
                        .heading(req.getHeading())
                        .description(req.getDescription())
                        .status(status)
                        .priority(priority)
                        .createdBy(author)
                        .assignedTo(executor)
                        .build()
        );
        return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Response> delete(Integer id) {

        Task task = taskRepository.getTaskById(id).orElseThrow(
                () -> new EntityNotFoundException("Task haven't found by id: " + id)
        );
        User user = securityService.getCurrentUser();
        if(securityService.isAdmin() || user.equals(task.getCreatedBy())) {
            taskRepository.deleteById(id);
            return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .error(Error.builder()
                            .techMessage("There are not enough permissions to delete this task")
                            .code(Code.FORBIDDEN)
                            .build())
                    .build(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<Response> changePriority(String heading, String priorityTitle) {

        User user = securityService.getCurrentUser();
        Task task = taskRepository.getTaskByHeading(heading).orElseThrow(
                () -> new EntityNotFoundException("Task haven't found with heading: " + heading)
        );

        if(securityService.isAdmin() || task.getCreatedBy().equals(user)) {

            Priority priority = priorityRepository.getPriorityByTitle(priorityTitle).orElseThrow(
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

        User user = securityService.getCurrentUser();
        Task task = taskRepository.getTaskByHeading(heading).orElseThrow(
                () -> new EntityNotFoundException("Task haven't found with heading: " + heading)
        );

        if(securityService.isAdmin() || task.getCreatedBy().equals(user)) {

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
