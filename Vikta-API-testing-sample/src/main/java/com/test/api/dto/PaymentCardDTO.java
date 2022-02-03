package com.test.api.dto;

import lombok.Data;

@Data
public class PaymentCardDTO{
    private Long id;
    private String cardNickName;
    private String cardNumber;
    private String cardCode;
    private String ownerName;
    private String expirationDate;
    private Long userId;
}
