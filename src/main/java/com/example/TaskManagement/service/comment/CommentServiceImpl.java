package com.example.TaskManagement.service.comment;

import com.example.TaskManagement.domain.api.comment.CommentReq;
import com.example.TaskManagement.domain.entity.Comment;
import com.example.TaskManagement.domain.entity.Task;
import com.example.TaskManagement.domain.response.Response;
import com.example.TaskManagement.domain.response.SuccessResponse;
import com.example.TaskManagement.repository.CommentRepository;
import com.example.TaskManagement.repository.TaskRepository;
import com.example.TaskManagement.service.security.utility.UtilitySecurityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    UtilitySecurityService securityService;
    TaskRepository taskRepository;

    @Override
    public ResponseEntity<Response> save(Integer taskId, CommentReq text) {

        Task task = taskRepository.getTaskById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task haven't found with id: " + taskId)
        );
        Comment comment = Comment.builder().commentText(text.getText()).build();
        task.getComments().add(comment);
        taskRepository.save(task);
        return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Response> edit(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Response> delete(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Response> getByTask(String heading) {
        return null;
    }
}
