package com.address.model.dto;


import com.address.model.enums.AddressType;
import lombok.Data;

@Data
public class AddressRequestDto {

    private Long id;
    private String street;
    private Long pincode;
    private String city;
    private String country;

    private AddressType addressType;


}
