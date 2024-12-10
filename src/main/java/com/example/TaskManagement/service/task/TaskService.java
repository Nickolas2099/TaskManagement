package com.example.TaskManagement.service.task;

import com.example.TaskManagement.domain.response.Response;
import org.springframework.http.ResponseEntity;

public interface TaskService {

    ResponseEntity<Response> getAllTasks();

    ResponseEntity<Response> getTasksByAuthor();

    ResponseEntity<Response> getTasksByAssignment();

    ResponseEntity<Response> editTask();

    ResponseEntity<Response> saveTask();

    ResponseEntity<Response> changePriority();

    ResponseEntity<Response> changeStatus();



}
