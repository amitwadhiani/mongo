package co.arctern.rider.api.service.sms;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    private static final String API_KEY = "Ac750b5013fc96f09a4904280423cb1af";
    private static final String SENDER = "ZMEDDO";
    private static final String BASE_URL = "https://alerts.solutionsinfini.co/api/v4/?";

    public String sendSms(String mob, String otp) {
        String message = "Your MEDDO One Time Password (OTP) is " + otp + " for your Login.\nPlease enter the digits only";
        SmsGateway smsGateway = new SmsGateway();
        Map<String, String> map = new HashMap<>();
        smsGateway.setApiKey(API_KEY);
        smsGateway.setSender(SENDER);
        smsGateway.setBaseURL(BASE_URL);
        map.put("format", "json");
        String str = null;
        try {
            str = smsGateway.sendSms(mob, message, map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }
}
