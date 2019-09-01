package co.arctern.api.provider.service;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * To retrieve token after validation using phone and otp
 */
public interface TokenService {

    public OAuth2AccessToken retrieveToken(String phone, String otp);
}
