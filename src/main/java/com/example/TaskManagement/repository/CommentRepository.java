package com.example.TaskManagement.repository;

import com.example.TaskManagement.domain.entity.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Integer> {

    Optional<Comment> getById(Integer id);
}
