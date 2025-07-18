package com.hit.userservice.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private Boolean success;
    private String message;
    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
