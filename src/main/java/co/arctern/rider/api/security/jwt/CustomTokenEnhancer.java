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

/**
 * For Bearer token generation through oauth/token
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Map<String, Object> additionalInfo = new HashMap<>();

        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }

        User user = (User) authentication.getPrincipal();
        Long userID = ((User) authentication.getPrincipal()).getId();
        additionalInfo.put("userID", userID);
        additionalInfo.put("name", user.getName());
        additionalInfo.put("email", user.getEmail());
        additionalInfo.put("phone", user.getPhone());
        additionalInfo.put("areaIds", user.getAreaIds());
        if (authorities.contains("SUPERUSER")) {
            // all area ids
            //      additionalInfo.put("doctorInClinicIds", doctorAndClinicControllerApi.getActiveDicsUsingGET());
        } else {
            //        additionalInfo.put("doctorInClinicIds", doctorInClinicIds);
        }
        additionalInfo.put("authorities", authorities);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}