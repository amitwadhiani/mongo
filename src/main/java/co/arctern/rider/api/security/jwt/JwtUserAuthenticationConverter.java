package co.arctern.rider.api.security.jwt;

import co.arctern.rider.api.security.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

class JwtUserAuthenticationConverter extends DefaultUserAuthenticationConverter implements UserAuthenticationConverter {
    private static final String roles = "roles";
    private static final String USER_ID = "userID";
    private static final String RIDER_AUTHORITY = "RIDER";
    private static final String CM_AUTHORITY = "CLUSTER_MANAGER";
    private static final String ADMIN = "ADMIN";
    private static final String SUPERUSER_AUTHORITY = "SUPERUSER";

    private Collection<? extends GrantedAuthority> defaultAuthorities;

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        if (!map.containsKey(AUTHORITIES)) {
            return defaultAuthorities;
        }
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
                    .collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
            User principal = new User((String) map.get(USERNAME), "N/A", authorities);
            if (authorities.contains(new SimpleGrantedAuthority(RIDER_AUTHORITY))) {
                principal.setRoles("ROLE_RIDER");
            } else if (authorities.contains(new SimpleGrantedAuthority(RIDER_AUTHORITY))
                    && authorities.contains(new SimpleGrantedAuthority(CM_AUTHORITY))) {
                principal.setRoles("ROLE_RIDER,ROLE_CM");
            } else {
                principal.setRoles("ROLE_SUPERUSER");
            }
            principal.setId(((Integer) map.get(USER_ID)).longValue());
            return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
        }
        return null;
    }
}
