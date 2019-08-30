package co.arctern.rider.api.util;

import co.arctern.rider.api.dao.LoginDao;
import co.arctern.rider.api.dao.UserDao;
import co.arctern.rider.api.domain.Login;
import co.arctern.rider.api.domain.User;
import co.arctern.rider.api.enums.OTPState;
import co.arctern.rider.api.service.OTPService;
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
    OTPService otpService;

    private static final Integer OTP_LENGTH = 6;

    @Transactional
    public String generateOTP(String phone) throws Exception {
        User user = userDao.findByPhone(phone).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Invalid phone number."));
        String pool = "0123456789";
        Random random = new Random();
        char[] chars = new char[OTP_LENGTH];
        int i = 0;
        while (i < 6) {
            chars[i] = pool.charAt(random.nextInt(pool.length()));
            i++;
        }
        String otp = new String(chars);
        if (smsService.sendSms(phone, otp) != null) {
            otpService.generateLogin(phone, otp, user);
        }
        return otp;
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
