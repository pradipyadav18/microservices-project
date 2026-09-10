package com.employee.model.dto;

import com.employee.model.enums.AddressType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long empId;
    private String street;
    private String pincode;
    private String city;
    private String country;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

}