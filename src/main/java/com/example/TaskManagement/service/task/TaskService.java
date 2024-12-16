package com.example.TaskManagement.service.task;

import com.example.TaskManagement.domain.api.task.TaskReq;
import com.example.TaskManagement.domain.response.Response;
import org.springframework.http.ResponseEntity;

public interface TaskService {

    ResponseEntity<Response> getByAuthor(String email, Integer page, Integer size);

    ResponseEntity<Response> getByAssignment(String executorEmail, Integer page, Integer size);

    ResponseEntity<Response> edit(Integer id, TaskReq task);

    ResponseEntity<Response> save(TaskReq task);

    ResponseEntity<Response> delete(Integer id);

    ResponseEntity<Response> changePriority(String heading, String priorityTitle);

    ResponseEntity<Response> changeStatus(String heading, String statusTitle);



}
