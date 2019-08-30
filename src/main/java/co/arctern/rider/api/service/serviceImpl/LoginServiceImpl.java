package co.arctern.rider.api.service.serviceImpl;

import co.arctern.rider.api.dao.LoginDao;
import co.arctern.rider.api.dao.UserDao;
import co.arctern.rider.api.domain.Login;
import co.arctern.rider.api.domain.User;
import co.arctern.rider.api.enums.OTPState;
import co.arctern.rider.api.service.LoginService;
import co.arctern.rider.api.util.OTPUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private OTPUtil otpGenerator;

    @Autowired
    UserDao userDao;

    @Autowired
    LoginDao loginDao;

    @SneakyThrows(Exception.class)
    @Transactional
    @Override
    public String generateOTP(String phone) {
        return otpGenerator.generateOTPForLogin(phone);
    }

    public void generateLogin(String phone, String otp, User user) {
        Login login = new Login();
        login.setContact(phone);
        login.setIsLogin(true);
        login.setUser(user);
        login.setStatus(OTPState.GENERATED);
        login.setGeneratedOTP(otp);
        loginDao.save(login);
    }

    @Override
    public String verifyOTP(String phone, String otp) {
        Login login = loginDao.findByGeneratedOTPAndStatusAndContact(otp, OTPState.GENERATED, phone);
        if (login != null) {
            return "Successfully verified.";
        }
        return "Wrong OTP entered.";
    }
}
