package com.test.api.dto;

import lombok.Data;

@Data
public class AddressDTO {
//  difference between documentation and code
    private Long id;
    private String street;  // in documentation streetName
    private String streetAdditional;    // in documentation streetNameAdditional
    private String cityName;
    private String regionName;
    private String postalCode;
    private String addressNickname;
    private Long userId;
}
