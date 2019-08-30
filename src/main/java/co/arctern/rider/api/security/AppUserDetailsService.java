package co.arctern.rider.api.security;

import co.arctern.rider.api.dao.UserDao;
import co.arctern.rider.api.security.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        co.arctern.rider.api.domain.User user = userDao.findByUsername(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(String.format("The username %username doesn't exist", username));
        });
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        User userDetails = new co.arctern.rider.api.security.model.User(user.getUsername(), user.getPassword(), authorities);
        userDetails.setId(user.getId());
        userDetails.setName(user.getName());
        userDetails.setEmail(user.getEmail());
        userDetails.setPhone(user.getPhone());
        userDetails.setRoles(StringUtils.join(",", user.getRoles()));
        return userDetails;
    }
}

