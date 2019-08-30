package co.arctern.rider.api.util;

import co.arctern.rider.api.dao.LoginDao;
import co.arctern.rider.api.dao.UserDao;
import co.arctern.rider.api.domain.Login;
import co.arctern.rider.api.domain.User;
import co.arctern.rider.api.enums.OTPStatus;
import co.arctern.rider.api.service.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPUtil {

    @Autowired
    SmsService smsService;

    @Autowired
    UserDao userDao;

    @Autowired
    LoginDao loginDao;

    private static final Integer OTP_LENGTH = 6;

    @Transactional
    public String generatorOTP(String phone) throws Exception {
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
            Optional<User> user = userDao.findByPhone(phone);
            Login login = new Login();
            login.setContact(phone);
            login.setGeneratedOTP(otp);
            login.setStatus(OTPStatus.CREATED);
            loginDao.save(login);
        }
        return otp;
    }

    @Transactional
    public Login verifyOTP(String number, String otp) {
        Login login = loginDao.findByGeneratedOTPAndStatusAndContact(otp, OTPStatus.GENERATED, number);
        if (login != null) {
            login.setStatus(OTPStatus.USED);
            return loginDao.save(login);
        }
        return null;
    }

}
