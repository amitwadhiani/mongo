package co.arctern.rider.api.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class TokenDecoder {

    private TokenStore tokenStore;

    public Long getUserId() {
        Map<String, Object> stringObjectMap = this.extractAccessToken();
        Object userID = stringObjectMap.get("USER_ID");
        if (userID == null) return null;
        return Long.parseLong(String.valueOf(userID));
    }


    public List<String> fetchRoles() {
        List<String> roles = new ArrayList<>();
        Map<String, Object> stringObjectMap = this.extractAccessToken();
        for (Map.Entry<String, Object> entryMap : stringObjectMap.entrySet()) {
            if (entryMap.getKey().equals("roles")) {
                roles.addAll((List<String>) entryMap.getValue());
            }
        }
        return roles;
    }

    private Map<String, Object> extractAccessToken() {
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication1.getDetails();
        String accessToken = oAuth2AuthenticationDetails.getTokenValue();
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
        Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
        return additionalInformation;
    }

}
