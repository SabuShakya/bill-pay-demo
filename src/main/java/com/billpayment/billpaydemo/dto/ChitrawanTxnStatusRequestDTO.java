package com.billpayment.billpaydemo.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChitrawanTxnStatusRequestDTO {

    private String username;

    @NotNull(message = "Amount is required.")
    @Min(0)
    private Double amount;

    @NotNull(message = "Transaction ID is required.")
    private String transactionId;
}
