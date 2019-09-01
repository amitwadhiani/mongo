package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.OTPState;
import co.arctern.api.provider.dao.LoginDao;
import co.arctern.api.provider.domain.Login;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.event.LoginEventHandler;
import co.arctern.api.provider.service.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private OtpService otpService;

    @Autowired
    LoginEventHandler loginEventHandler;

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginStateFlowService loginStateFlowService;

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
            if (login.getCreatedAt().getTime() - new Date(System.currentTimeMillis() - 1 * 60 * 1000).getTime() > 0) {
                login.setStatus(OTPState.USED);
                loginStateFlowService.createLoginStateFlow(loginDao.save(login), true);
                loginEventHandler.markLoggedInStateForUser(userService.fetchUserByPhone(phone), true);
                return tokenService.retrieveToken(phone, otp);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired. Please request an OTP again.");
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong OTP. Please try again.");
    }

    public StringBuilder logOut(Long userId) {
        User user = userService.fetchUser(userId);
        Login login = loginDao.findByUserIdAndStatusAndContact(userId, OTPState.USED, user.getPhone())
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not logged in.");
                });
        loginStateFlowService.createLoginStateFlow(login, false);
        loginEventHandler.markLoggedInStateForUser(user, false);
        return SUCCESS_MESSAGE;
    }
}
