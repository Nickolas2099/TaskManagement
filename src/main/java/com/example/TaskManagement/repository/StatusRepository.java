package com.example.TaskManagement.repository;

import com.example.TaskManagement.domain.entity.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StatusRepository extends CrudRepository<Status, Integer> {

    Optional<Status> getStatusByTitle(String Title);
}
