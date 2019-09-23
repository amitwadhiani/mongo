package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.OTPState;
import co.arctern.api.provider.constant.UserState;
import co.arctern.api.provider.dao.LoginDao;
import co.arctern.api.provider.domain.Login;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.eventhandler.LoginEventHandler;
import co.arctern.api.provider.service.LoginService;
import co.arctern.api.provider.service.OtpService;
import co.arctern.api.provider.service.TokenService;
import co.arctern.api.provider.service.UserService;
import co.arctern.api.provider.util.DateUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
public class LoginServiceImpl implements LoginService {

    private final OtpService otpService;
    private final LoginEventHandler loginEventHandler;
    private final LoginDao loginDao;
    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public LoginServiceImpl(OtpService otpService,
                            LoginEventHandler loginEventHandler,
                            LoginDao loginDao,
                            TokenService tokenService,
                            UserService userService) {
        this.otpService = otpService;
        this.loginEventHandler = loginEventHandler;
        this.loginDao = loginDao;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @SneakyThrows(Exception.class)
    @Transactional
    @Override
    public StringBuilder generateOTP(String phone, Boolean isAdmin) {
        if (isAdmin) {
            if (userService.fetchUser(phone).getUserRoles().stream()
                    .filter(a -> a.getRole().getRole().equalsIgnoreCase("ROLE_ADMIN"))
                    .findAny().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ONLY_ADMIN_LOGIN_ALLOWED_MESSAGE.toString());
            }
            return otpService.generateOTPForLogin(phone);
        }
        return otpService.generateOTPForLogin(phone);
    }

    @Override
    public void generateLogin(String phone, String otp, User user) {
        Login login = new Login();
        login.setContact(phone);
        login.setUser(user);
        login.setStatus(OTPState.GENERATED);
        login.setGeneratedOTP(otp);
        login.setUserState(UserState.EXISTING);
        loginDao.save(login);
    }

    @Override
    @SneakyThrows(Exception.class)
    @Transactional
    public OAuth2AccessToken verifyOTP(String phone, String otp) {
        Login login = loginDao.findByGeneratedOTPAndStatusAndContact(otp, OTPState.GENERATED, phone);
        if (login != null) {
            if (login.getCreatedAt().getTime() - DateUtil.fetchDifferenceFromCurrentDateInMs(1) > 0) {
                login.setStatus(OTPState.USED);
                login.setLoginState(true);
                OAuth2AccessToken oAuth2AccessToken = tokenService.retrieveToken(phone, otp);
                loginDao.save(login);
                loginEventHandler.markLoggedInStateForUser(userService.fetchUserByPhone(phone), true);
                userService.saveLastLoginTime(phone, login.getCreatedAt());
                return oAuth2AccessToken;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EXPIRED_OTP_MESSAGE.toString());
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_OTP_MESSAGE.toString());
    }

    @Override
    public StringBuilder logOut(Long userId) {
        User user = userService.fetchUser(userId);
        Login login = loginDao.findByUserIdAndStatusAndContact(userId, OTPState.USED, user.getPhone())
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_LOGGED_IN_MESSAGE.toString());
                });
        login.setLoginState(false);
        login.setLogoutTime(DateUtil.CURRENT_TIMESTAMP);
        loginDao.save(login);
        loginEventHandler.markLoggedInStateForUser(user, false);
        return SUCCESS_MESSAGE;
    }
}
