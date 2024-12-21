package com.example.TaskManagement.service.comment;

import com.example.TaskManagement.domain.api.comment.CommentReq;
import com.example.TaskManagement.domain.constant.Code;
import com.example.TaskManagement.domain.entity.Comment;
import com.example.TaskManagement.domain.entity.Task;
import com.example.TaskManagement.domain.response.Response;
import com.example.TaskManagement.domain.response.SuccessResponse;
import com.example.TaskManagement.domain.response.error.Error;
import com.example.TaskManagement.domain.response.error.ErrorResponse;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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
    public ResponseEntity<Response> edit(Integer id, CommentReq text) {

        Comment comment = commentRepository.getById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment haven't found with id: " + id)
        );
        if(securityService.getCurrentUser().equals(comment.getUser())) {

            comment.setCommentText(text.getText());
            return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(ErrorResponse.builder().error(Error
                    .builder()
                    .code(Code.FORBIDDEN)
                    .techMessage("There are not enough permissions to edit this comment")
                    .build()).build(), HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<Response> delete(Integer id) {

        Comment comment = commentRepository.getById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment haven't found with id: " + id)
        );
        if(securityService.isAdmin() || securityService.getCurrentUser().equals(comment.getUser())) {

            commentRepository.delete(comment);
            return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(ErrorResponse.builder().error(Error
                    .builder()
                    .code(Code.FORBIDDEN)
                    .techMessage("There are not enough permissions to delete this comment")
                    .build()).build(), HttpStatus.FORBIDDEN);
        }
    }

}
