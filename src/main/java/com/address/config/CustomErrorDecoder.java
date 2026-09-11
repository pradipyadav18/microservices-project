package com.address.config;

import com.address.exception.BadRequestException;
import com.address.exception.CustomException;
import com.address.exception.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import tools.jackson.databind.ObjectMapper;
import com.address.exception.CustomException;
import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        int status=response.status();
        if(status==503 || status==500){
            return new BadRequestException("Employee service is down . Please try later " , HttpStatus.SERVICE_UNAVAILABLE);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream is = response.body().asInputStream()) {

            ErrorResponse errorResponse =
                    objectMapper.readValue(is, ErrorResponse.class);

            return new CustomException(
                    errorResponse.getMessage(),
                    errorResponse.getStatus()
            );

        } catch (IOException e) {

            return new CustomException(
                    "INTERNAL_SERVER_ERROR"
            );
        }
    }
}