package co.arctern.api.provider.security.jwt;

import co.arctern.api.provider.security.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Setting additional details to jwt token through Security User model.
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Map<String, Object> additionalInfo = new HashMap<>();
        List<String> lss = new ArrayList<>();
        for (GrantedAuthority ga : authentication.getAuthorities()) {
            lss.add(ga.getAuthority());
        }
        User user = (User) authentication.getPrincipal();
        Long userID = ((User) authentication.getPrincipal()).getId();
        additionalInfo.put("userID", userID);
        additionalInfo.put("name", user.getName());
        additionalInfo.put("email", user.getEmail());
        additionalInfo.put("phone", user.getPhone());
        additionalInfo.put("areaIds", user.getAreaIds());
        additionalInfo.put("authorities", lss);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}