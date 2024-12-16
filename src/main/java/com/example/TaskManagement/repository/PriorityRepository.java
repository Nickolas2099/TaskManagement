package com.example.TaskManagement.repository;

import com.example.TaskManagement.domain.entity.Priority;
import com.example.TaskManagement.domain.entity.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PriorityRepository extends CrudRepository <Priority, Integer> {

    Optional<Priority> getPrioritiesByTitle(String title);

    Optional<Priority> getPriorityById(Integer id);
}
