package co.arctern.api.provider.security.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * security model class for jwt token
 */
@Data
public class User extends org.springframework.security.core.userdetails.User {

    private String username;
    private String phone;
    private String name;
    private String email;
    private Long id;
    private String roles;
    private List<Long> clusterIds;

    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public User(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}
