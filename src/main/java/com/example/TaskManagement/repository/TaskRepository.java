package com.example.TaskManagement.repository;

import com.example.TaskManagement.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(nativeQuery = true, value = """
            SELECT * 
            FROM task t
            JOIN status s ON t.status_id = s.id
            WHERE s.title NOT ILIKE 'завершено'
            """)
    List<Task> getAll();
}
