package com.example.TaskManagement.domain.api.task;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskReq {

    @Size(max = 255)
    @NotBlank(message = "heading is required")
    String heading;

    @Size(max = 255)
    @NotBlank(message = "description is required")
    String description;

    @NotBlank(message = "statusTitle is required")
    String statusTitle;

    @NotBlank(message = "priorityTitle is required")
    String priorityTitle;

    @Email
    @NotBlank(message = "executorEmail is required")
    String executorEmail;
}
