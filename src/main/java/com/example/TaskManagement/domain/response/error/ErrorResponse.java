package com.example.TaskManagement.domain.response.error;

import com.example.TaskManagement.domain.response.Response;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse implements Response {

    private Error error;
}
