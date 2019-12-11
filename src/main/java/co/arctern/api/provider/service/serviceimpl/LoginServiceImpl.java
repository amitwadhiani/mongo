package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.constant.LoginState;
import co.arctern.api.provider.constant.OTPState;
import co.arctern.api.provider.constant.UserState;
import co.arctern.api.provider.dao.LoginDao;
import co.arctern.api.provider.domain.Login;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.eventhandler.LoginEventHandler;
import co.arctern.api.provider.service.*;
import co.arctern.api.provider.sms.SmsService;
import co.arctern.api.provider.util.DateUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    private final OtpService otpService;
    private final LoginEventHandler loginEventHandler;
    private final LoginDao loginDao;
    private final TokenService tokenService;
    private final UserService userService;
    private final SmsService smsService;
    private final LoginFlowService loginFlowService;

    @Autowired
    public LoginServiceImpl(OtpService otpService,
                            LoginEventHandler loginEventHandler,
                            LoginDao loginDao,
                            TokenService tokenService,
                            UserService userService, SmsService smsService,
                            LoginFlowService loginFlowService) {
        this.otpService = otpService;
        this.loginEventHandler = loginEventHandler;
        this.loginDao = loginDao;
        this.tokenService = tokenService;
        this.userService = userService;
        this.smsService = smsService;
        this.loginFlowService = loginFlowService;
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
            return generateOTPForLogin(phone);
        }
        return generateOTPForLogin(phone);
    }

    @Transactional
    @Override
    public StringBuilder generateOTPForLogin(String phone) {
        User user = userService.fetchUser(phone);
        String otp = otpService.getOtpString();
        if (smsService.sendSms(phone, otp) != null) {
            generateLogin(phone, otp, user);
        }
        return SUCCESS_MESSAGE;
    }

    @Override
    public void generateLogin(String phone, String otp, User user) {
        Login login = new Login();
        login.setContact(phone);
        login.setUser(user);
        login.setStatus(OTPState.GENERATED);
        login.setGeneratedOTP(otp);
        login.setUserState(UserState.EXISTING);
        login = loginDao.save(login);
        loginFlowService.create(login, LoginState.ATTEMPTED);
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
                login = loginDao.save(login);
                loginFlowService.create(login, LoginState.LOGIN);
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
        List<Login> logins = loginDao.findByUserIdAndStatusAndContactOrderByCreatedAtDesc(userId, OTPState.USED, user.getPhone());
        if (!CollectionUtils.isEmpty(logins)) {
            Login login = logins.get(0);
            login.setLoginState(false);
            login.setLogoutTime(DateUtil.CURRENT_TIMESTAMP);
            login = loginDao.save(login);
            loginFlowService.create(login, LoginState.LOGOUT);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_LOGGED_IN_MESSAGE.toString());
        }
        loginEventHandler.markLoggedInStateForUser(user, false);
        return SUCCESS_MESSAGE;
    }
}
