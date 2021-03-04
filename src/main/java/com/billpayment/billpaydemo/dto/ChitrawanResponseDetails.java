package com.billpayment.billpaydemo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ChitrawanResponseDetails implements Serializable {

    private ChitrawanCustomerDetails customer;

    private ChitrawanCurrentSubscription current_subscription;

    private ChitrawanDueResponse due;

    private List<ChitrawanPackages> packages;

}
