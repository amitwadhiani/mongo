package co.arctern.api.provider.service;

import co.arctern.api.provider.util.MessageUtil;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * To retrieve token after validation using phone and otp
 */
public interface TokenService  extends MessageUtil {

    public OAuth2AccessToken retrieveToken(String phone, String otp);
}
