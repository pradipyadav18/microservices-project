package com.auth.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JwtTokenResponse {

    private String token;
    private String  type;
    private String validUntil;


}
