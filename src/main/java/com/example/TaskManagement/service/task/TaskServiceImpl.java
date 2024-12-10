package com.example.TaskManagement.service.task;

import com.example.TaskManagement.domain.response.Response;
import com.example.TaskManagement.repository.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepository;

    @Override
    public ResponseEntity<Response> getAllTasks() {

        //taskRepository.getAll();
        return null;
    }

    @Override
    public ResponseEntity<Response> getTasksByAuthor() {
        return null;
    }

    @Override
    public ResponseEntity<Response> getTasksByAssignment() {
        return null;
    }

    @Override
    public ResponseEntity<Response> editTask() {
        return null;
    }

    @Override
    public ResponseEntity<Response> saveTask() {
        return null;
    }

    @Override
    public ResponseEntity<Response> changePriority() {
        return null;
    }

    @Override
    public ResponseEntity<Response> changeStatus() {
        return null;
    }
}
