package com.employee.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BadRequestException extends RuntimeException{

    private String message;
    private HttpStatus status;

    public BadRequestException(String message){
        this.message=message;
        this.status=HttpStatus.BAD_REQUEST;
    }

}
