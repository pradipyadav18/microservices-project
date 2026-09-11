package com.address.model.entity;

import com.address.model.enums.AddressType;
import jakarta.persistence.*;
        import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long empId;
    private String street;
    private Long pincode;
    private String city;
    private String country;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;
}