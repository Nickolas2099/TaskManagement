package com.example.TaskManagement.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CurrentTimestamp;

import java.sql.Timestamp;

@Table(name = "comment")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String commentText;

    @CurrentTimestamp
    Timestamp creationTime;

    @ManyToOne
    User user;

}
