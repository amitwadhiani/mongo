package co.arctern.rider.api.security.jwt;


import co.arctern.rider.api.security.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority role : authentication.getAuthorities()) {
            roles.add(role.getAuthority());
        }
        User usr = (User) authentication.getPrincipal();
        Long userID = ((User) authentication.getPrincipal()).getId();
        additionalInfo.put("userID", userID);
        additionalInfo.put("phone", usr.getPhone());
        additionalInfo.put("username", usr.getUsername());
        additionalInfo.put("authorities", roles);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
