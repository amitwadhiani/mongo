package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface LoginService {

    /**
     * to generate otp for a phone number.
     *
     * @param phone
     * @return
     */
    public String generateOTP(String phone);

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
}
