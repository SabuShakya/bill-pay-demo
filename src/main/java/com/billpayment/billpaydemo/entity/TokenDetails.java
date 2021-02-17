package com.billpayment.billpaydemo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "token_details")
@Getter
@Setter
public class TokenDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "access_token",nullable = false)
    private String accessToken;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "refresh_token")
    private String refresh_token;

    @Column(name = "exprires_in")
    private Integer expiresIn;

    @Column(name = "scope")
    private String scope;

    @Column(name = "requested_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestedDate;

    @Column(name = "status")
    private Character status;
}
