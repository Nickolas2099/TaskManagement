package com.example.TaskManagement.repository;

import com.example.TaskManagement.domain.entity.Task;
import com.example.TaskManagement.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    Page<Task> getTasksByCreatedBy(User author, Pageable pageable);

    Page<Task> getTasksByAssignedTo(User executor, Pageable pageable);

    Optional<Task> getTaskByHeading(String heading);

    Optional<Task> getTaskById(Integer id);

    @Query(nativeQuery = true, value = """
        INSERT INTO task(heading, description, statusId, priorityId, authorId, executorId)
        VALUES(:heading, :description, :statusId, :priorityId, :authorId, :executorId)
    """)
    void save(@Param("heading") String heading, @Param("description") String description,
              @Param("statusId") Integer statusId, @Param("priorityId") Integer priorityId,
              @Param("authorId") Integer authorId, @Param("executorId") Integer executorId);


}
