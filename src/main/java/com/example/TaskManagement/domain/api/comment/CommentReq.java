package com.example.TaskManagement.domain.api.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentReq {

    @Size(min = 8, max = 500, message = "email out of symbols borders. min is 8, max is 500")
    @NotBlank(message = "text is required")
    private String text;

}
