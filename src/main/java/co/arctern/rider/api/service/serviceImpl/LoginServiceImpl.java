package co.arctern.rider.api.service.serviceImpl;

import co.arctern.rider.api.dao.LoginDao;
import co.arctern.rider.api.domain.Login;
import co.arctern.rider.api.domain.User;
import co.arctern.rider.api.enums.OTPState;
import co.arctern.rider.api.service.LoginService;
import co.arctern.rider.api.util.OTPUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private OTPUtil otpGenerator;

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
    @SneakyThrows(HttpClientErrorException.BadRequest.class)
    public String verifyOTP(String phone, String otp) {
        Login login = loginDao.findByGeneratedOTPAndStatusAndContact(otp, OTPState.GENERATED, phone);
        if (login != null) {
            login.setStatus(OTPState.USED);
            return "Successfully verified.";
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong OTP. Please try again.");
    }
}
