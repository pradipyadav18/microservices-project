package com.employee.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class MissingParameterException  extends  RuntimeException{

    private String message;
    private HttpStatus status;

    public MissingParameterException(String message){
        this.message=message;
        this.status=HttpStatus.BAD_REQUEST;
    }



}
