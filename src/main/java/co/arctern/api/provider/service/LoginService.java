package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface LoginService extends MessageUtil {

    /**
     * to generate otp for a phone number.
     *
     * @param phone
     * @param isAdmin
     * @return
     */
    public String generateOTP(String phone,Boolean isAdmin);

    /**
     * to verify otp from db with provided otp using phone number.
     *
     * @param phone
     * @param otp
     * @return
     */
    public OAuth2AccessToken verifyOTP(String phone, String otp);

    /**
     * to generate login using phone and otp for a user.
     *
     * @param phone
     * @param otp
     * @param user
     */
    public void generateLogin(String phone, String otp, User user);

    /**
     * to log out of the system for a user.
     *
     * @param userId
     */
    public StringBuilder logOut(Long userId);
}
