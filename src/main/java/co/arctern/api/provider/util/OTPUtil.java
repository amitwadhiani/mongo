package co.arctern.api.provider.util;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.service.LoginService;
import co.arctern.api.provider.service.UserService;
import co.arctern.api.provider.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

/**
 * otp util to generate otp for various use cases in the api server.
 */
@Service
public class OTPUtil {

    @Autowired
    SmsService smsService;

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    private static final Integer OTP_LENGTH = 6;

    @Transactional
    public String generateOTPForLogin(String phone) throws Exception {
        User user = userService.fetchUser(phone);
        String otp = getOtpString();
        if (smsService.sendSms(phone, otp) != null) {
            loginService.generateLogin(phone, otp, user);
        }
        return otp;
    }

    @Transactional
    public String generateOTPForRating(String phone) throws Exception {
        User user = userService.fetchUserByPhone(phone);
        // TODO
        // handle equal otp values for satisfied and not satisfied cases
        if (smsService.sendSmsForRating(phone, getOtpString(), getOtpString()) != null) {
            return "Success";
        }
        return "Please try again";
    }

    public String getOtpString() {
        String pool = "0123456789";
        Random random = new Random();
        char[] chars = new char[OTP_LENGTH];
        int i = 0;
        while (i < 6) {
            chars[i] = pool.charAt(random.nextInt(pool.length()));
            i++;
        }
        return new String(chars);
    }

}
