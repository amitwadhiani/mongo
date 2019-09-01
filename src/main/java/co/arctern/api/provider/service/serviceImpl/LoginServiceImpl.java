package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.LoginDao;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.constant.OTPState;
import co.arctern.api.provider.service.OtpService;
import co.arctern.api.provider.service.TokenService;
import co.arctern.api.provider.util.OTPUtil;
import co.arctern.api.provider.domain.Login;
import co.arctern.api.provider.service.LoginService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private OtpService otpService;

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private TokenService tokenService;

    @SneakyThrows(Exception.class)
    @Transactional
    @Override
    public String generateOTP(String phone) {
        return otpService.generateOTPForLogin(phone);
    }

    public void generateLogin(String phone, String otp, User user) {
        Login login = new Login();
        login.setContact(phone);
        login.setUser(user);
        login.setStatus(OTPState.GENERATED);
        login.setGeneratedOTP(otp);
        loginDao.save(login);
    }

    @Override
    @SneakyThrows(Exception.class)
    public OAuth2AccessToken verifyOTP(String phone, String otp) {
        Login login = loginDao.findByGeneratedOTPAndStatusAndContact(otp, OTPState.GENERATED, phone);
        if (login != null) {
            login.setStatus(OTPState.USED);
            loginDao.save(login);
            return tokenService.retrieveToken(phone, otp);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong OTP. Please try again.");
    }
}
