package com.billpayment.billpaydemo.utils;

import com.billpayment.billpaydemo.entity.TokenDetails;

import java.util.Date;

public class TokenValidationUtil {

    public static boolean isTokenExpired(TokenDetails tokenDetails) {

        Date requestedDate = tokenDetails.getRequestedDate();

        Date currentDate = new Date();

        long timeDifferenceInMilliseconds = currentDate.getTime() - requestedDate.getTime();

        if (timeDifferenceInMilliseconds> tokenDetails.getExpiresIn())
            return true;

        return false;
    }
}
