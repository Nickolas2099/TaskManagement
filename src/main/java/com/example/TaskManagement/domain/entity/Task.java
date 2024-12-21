package com.example.TaskManagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Table(name = "task")
@Entity
@Data
@Builder
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
    @JoinColumn(name = "created_by")
    User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    User assignedTo;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Comment> comments;

}
