package co.arctern.rider.api.service.serviceImpl;

import co.arctern.rider.api.dao.LoginDao;
import co.arctern.rider.api.dao.UserDao;
import co.arctern.rider.api.util.OTPUtil;
import co.arctern.rider.api.service.OTPService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OTPServiceImpl implements OTPService {

    @Autowired
    private OTPUtil otpGenerator;

    @Autowired
    UserDao userDao;

    @Autowired
    LoginDao loginDao;

    @SneakyThrows(Exception.class)
    public String generateOTP(String phone) {
        String otp = otpGenerator.generatorOTP(phone);
        //TODO
        return otp;
    }

    public String verifyOTP(String phone, String otp) {
        //TODO
        if (otp != null) {
            return "Successfully verified";
        }
        return "Wrong OTP";
    }
}
