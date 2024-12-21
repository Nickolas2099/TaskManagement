package com.example.TaskManagement.service.comment;

import com.example.TaskManagement.domain.api.comment.CommentReq;
import com.example.TaskManagement.domain.response.Response;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    ResponseEntity<Response> save(Integer taskId, CommentReq text);
    ResponseEntity<Response> edit(Integer id, CommentReq text);
    ResponseEntity<Response> delete(Integer id);

}
