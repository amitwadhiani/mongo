package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.AreaDao;
import co.arctern.api.provider.dao.UserDao;
import co.arctern.api.provider.domain.Area;
import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.service.TokenService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private TokenStore tokenStore;

    @Value("${security.jwt.client-id}")
    private String clientID;

    @Value("${security.jwt.client-secret}")
    private String secret;

    @Autowired
    UserDao userDao;

    @Autowired
    AreaDao areaDao;

    @Override
    @SneakyThrows(Exception.class)
    public OAuth2AccessToken retrieveToken(String phone, String otp) {
        User user = userDao.findByPhone(phone).get();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", clientID);
        parameters.put("grant_type", "password");
        parameters.put("password", user.getPassword());
        parameters.put("username", user.getPhone());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        List<Role> roles = user.getUserRoles().stream().map(a -> a.getRole()).collect(Collectors.toList());
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        Principal principal = new UsernamePasswordAuthenticationToken(clientID, secret, authorities);
        return tokenEndpoint.postAccessToken(principal, parameters).getBody();
    }

    /**
     * fetch user id from token.
     *
     * @return
     */
    @Override
    public Long fetchUserId() {
        Map<String, Object> stringObjectMap = this.extractAccessToken();
        Object userID = stringObjectMap.get("userID");
        if (userID == null) return null;
        return Long.parseLong(String.valueOf(userID));
    }

    /**
     * fetch areas from token.
     *
     * @return
     */
    @Override
    public List<Area> fetchAreasFromToken() {
        if (this.fetchAreaIds() != null)
            return areaDao.findByIdIn(this.fetchAreaIds());
        return null;
    }

    /**
     * to fetch area ids from token.
     *
     * @return
     */
    @Override
    public List<Long> fetchAreaIds() {
        List<Long> areaIds = new ArrayList<>();
        Map<String, Object> stringObjectMap = this.extractAccessToken();
        for (Map.Entry<String, Object> entryMap : stringObjectMap.entrySet()) {
            if (entryMap.getKey().equals("areaIds")) {
                areaIds.addAll((List<Long>) entryMap.getValue());
            }
        }
        return areaIds;
    }

    /**
     * to fetch authorities from token.
     *
     * @return
     */
    @Override
    public List<String> fetchAuthorities() {
        List<String> authorities = new ArrayList<>();
        Map<String, Object> stringObjectMap = this.extractAccessToken();
        for (Map.Entry<String, Object> entryMap : stringObjectMap.entrySet()) {
            if (entryMap.getKey().equals("authorities")) {
                authorities.addAll((List<String>) entryMap.getValue());
            }
        }
        return authorities;
    }

    /**
     * to extract access token.
     *
     * @return
     */
    @Override
    public Map<String, Object> extractAccessToken() {
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication1.getDetails();
        String accessToken = oAuth2AuthenticationDetails.getTokenValue();
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
        Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
        return additionalInformation;
    }
}
