package com.employee.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResourceNotFoundException extends RuntimeException{

    private String message;
    private HttpStatus status;


    public ResourceNotFoundException(String message) {
        this.message = message;
        this.status = HttpStatus.NOT_FOUND;
    }


}
