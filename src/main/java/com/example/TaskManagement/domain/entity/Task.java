package com.example.TaskManagement.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Table(name = "task")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {

    @Id
    Integer id;

    String heading;

    String description;

    String status;

    String priority;

    @OneToMany
    List<Comment> comments;

    @ManyToOne
    User createdBy;

    @ManyToOne
    User assignedTo;

}
