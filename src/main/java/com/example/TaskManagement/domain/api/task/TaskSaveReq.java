package com.example.TaskManagement.domain.api.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskSaveReq {

    @Size(max = 255)
    @NotBlank(message = "heading is required")
    String heading;

    @Size(max = 255)
    @NotBlank(message = "description is required")
    String description;

    @NotBlank(message = "statusId is required")
    Integer statusId;

    @NotBlank(message = "priority is required")
    Integer priorityId;

    @NotNull(message = "authorEmail is required")
    Integer authorId;

    @NotNull(message = "executorEmail is required")
    Integer executorId;
}
