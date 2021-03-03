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
        String CREATE_CLIENT = "/createClient";
    }

    interface ChitrawanVenderApiConstants {
        String CHITRAWAN_API_BASE = "https://kcmstest.doennet.com.np/api/payment";
        String FETCH_USER_DETAILS = CHITRAWAN_API_BASE + "/getUserDetails";
        String ACTIVATE_PAYMENT = CHITRAWAN_API_BASE + "/activate";
        String CHECK_TRANSACTION_STATUS = CHITRAWAN_API_BASE + "/checkTransactionStatus";
    }

    interface ChitrawanApiConstants {
        String CHITRAWAN_BASE = "/chitrawan";
        String GET_USER_DETAILS = CHITRAWAN_BASE + "/getUserDetails";
        String CHITRAWAN_BILL_PAY = "/payment";
        String CHITRAWAN_CHECK_TRANSACTION_STATUS = "/checkTransactionStatus";
    }
}
