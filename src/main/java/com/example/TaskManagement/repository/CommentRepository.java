package com.example.TaskManagement.repository;

import com.example.TaskManagement.domain.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Integer> {


}
