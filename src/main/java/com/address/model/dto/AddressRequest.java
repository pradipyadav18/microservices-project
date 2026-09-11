package com.address.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddressRequest {

    private Long empId;
    private List<AddressRequestDto>addressRequestDtoList;


}
