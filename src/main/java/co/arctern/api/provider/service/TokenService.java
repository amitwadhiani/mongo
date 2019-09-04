package co.arctern.api.provider.service;

import co.arctern.api.provider.util.MessageUtil;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * To retrieve token after validation using phone and otp
 */
public interface TokenService extends MessageUtil {

    /**
     * to retrieve token via username and password(otp, in our case).
     *
     * @param phone
     * @param otp
     * @return
     */
    public OAuth2AccessToken retrieveToken(String phone, String otp);
}
