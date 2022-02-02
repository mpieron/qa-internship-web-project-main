package com.test.api.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String loginName;
    private String password;
    private String email;
    private String firstName;
    private String middleName;
    private String surname;
    private String pathToAvatarImage;
    private int[] addressIds;
    private int[] paymentCardIds;
}
