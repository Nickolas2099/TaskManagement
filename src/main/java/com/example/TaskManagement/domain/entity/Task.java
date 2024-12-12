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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String heading;

    String description;

    @ManyToOne
    Status status;

    @ManyToOne
    Priority priority;

    @ManyToOne
    User createdBy;

    @ManyToOne
    User assignedTo;

    @OneToMany
    List<Comment> comments;

}
