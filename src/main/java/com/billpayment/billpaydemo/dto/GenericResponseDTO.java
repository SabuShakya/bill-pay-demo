package com.billpayment.billpaydemo.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenericResponseDTO {

    private String resultCode;

    private String resultDescription;

    private Object data;
}
