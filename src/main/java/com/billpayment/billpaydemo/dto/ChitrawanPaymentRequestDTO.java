package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChitrawanPaymentRequestDTO {

    @NotNull(message = "Username cannot be null.")
    private String username;

    private String packageId;

    @Min(value = 0, message = "Amount must not be less than 0.")
    private Double amount;

    @NotEmpty
    private String transactionId;

    @NotNull(message = "Request id cannot be null.")
    private String requestId;
}
