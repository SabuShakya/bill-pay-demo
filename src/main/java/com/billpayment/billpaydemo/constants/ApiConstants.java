package com.billpayment.billpaydemo.constants;

public interface ApiConstants {

    String API_SERVER = "http://202.63.242.139:8000";

    String API_BASE = API_SERVER.concat("/api/v2");

    String FETCH_TOKEN = API_SERVER.concat("/oauth/token");

    String BILL_STATEMENT = API_BASE.concat("/billstatement");

    String BILL_RECEIPT_V2 = API_BASE.concat("/receipt");

    String BASE = "/api";

    interface TokenConstants {
        String GET_TOKEN = "/getToken";
    }

    interface BillApiConstants {
        String FETCH_BILL_STATEMENT = "/billStatement";
        String PAY_BILL = "/payBill/receipt";
    }

    interface ClientApiConstants {
        String CREATE_CLIENT = "createClient";
    }
}
