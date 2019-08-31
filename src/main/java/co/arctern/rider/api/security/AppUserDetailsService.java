package co.arctern.rider.api.security;

import co.arctern.rider.api.dao.UserDao;
import co.arctern.rider.api.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Setting details in the jwt token through user entity
 */
@Component
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        co.arctern.rider.api.domain.User user = userDao.findByPhone(phone)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException(String.format("The username %phone doesn't exist", phone));
                });
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        });
        User userDetails = new co.arctern.rider.api.security.model.User(user.getUsername(), user.getPassword(), authorities);
        userDetails.setId(user.getId());
        userDetails.setName(user.getName());
        userDetails.setEmail(user.getEmail());
        userDetails.setPhone(user.getPhone());
        return userDetails;
    }
}
