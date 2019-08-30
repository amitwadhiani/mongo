package co.arctern.rider.api.util;

import co.arctern.rider.api.dao.LoginDao;
import co.arctern.rider.api.dao.UserDao;
import co.arctern.rider.api.domain.Login;
import co.arctern.rider.api.domain.User;
import co.arctern.rider.api.enums.OTPState;
import co.arctern.rider.api.service.LoginService;
import co.arctern.rider.api.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Random;

@Service
public class OTPUtil {

    @Autowired
    SmsService smsService;

    @Autowired
    UserDao userDao;

    @Autowired
    LoginDao loginDao;

    @Autowired
    LoginService loginService;

    private static final Integer OTP_LENGTH = 6;

    @Transactional
    public String generateOTPForLogin(String phone) throws Exception {
        User user = userDao.findByPhone(phone).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "User not registered in the system."));
        String otp = getOtpString();
        if (smsService.sendSms(phone, otp) != null) {
            loginService.generateLogin(phone, otp, user);
        }
        return otp;
    }

    @Transactional
    public String generateOTPForRating(String phone) throws Exception {
        User user = userDao.findByPhone(phone).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Invalid phone number."));
        // TODO
        // handle equal otps for yes and no
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

    @Transactional
    public Login verifyOTP(String number, String otp) {
        Login login = loginDao.findByGeneratedOTPAndStatusAndContact(otp, OTPState.GENERATED, number);
        if (login != null) {
            login.setStatus(OTPState.USED);
            return loginDao.save(login);
        }
        return null;
    }

}
