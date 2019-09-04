package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.UserDao;
import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.service.TokenService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Value("${security.jwt.client-id}")
    private String clientID;

    @Value("${security.jwt.client-secret}")
    private String secret;

    @Autowired
    UserDao userDao;

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

}
