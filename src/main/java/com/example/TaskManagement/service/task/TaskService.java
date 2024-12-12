package com.example.TaskManagement.service.task;

import com.example.TaskManagement.domain.api.task.TaskSaveReq;
import com.example.TaskManagement.domain.entity.Task;
import com.example.TaskManagement.domain.response.Response;
import org.springframework.http.ResponseEntity;

public interface TaskService {

    ResponseEntity<Response> getTasksByAuthor(String email, Integer page, Integer size);

    ResponseEntity<Response> getTasksByAssignment(String executorEmail, Integer page, Integer size);

    ResponseEntity<Response> editTask(String heading, Task task);

    ResponseEntity<Response> saveTask(TaskSaveReq task);

    ResponseEntity<Response> changePriority(String heading, String priorityTitle);

    ResponseEntity<Response> changeStatus(String heading, String statusTitle);



}
