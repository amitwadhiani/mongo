package co.arctern.rider.api.security.jwt;

import co.arctern.rider.api.dao.UserDao;
import co.arctern.rider.api.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${security.jwt.client-id}")
    private String clientID;

    @Value("${security.jwt.client-secret}")
    private String secret;

    @Autowired
    private UserDao userDao;

    public OAuth2AccessToken retrieveToken(String username, String password) throws Exception {
        User user = userDao.findByPhone(username).get();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", clientID);
        parameters.put("grant_type", "password");
        parameters.put("password", user.getPassword());
        parameters.put("username", user.getPhone());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(String.join(",", user.getRoles().stream().map(a -> a.getRole()).collect(Collectors.toList()))));
        Principal principal = new UsernamePasswordAuthenticationToken(clientID, secret, authorities);
        return new TokenEndpoint().postAccessToken(principal, parameters).getBody();
    }

}

