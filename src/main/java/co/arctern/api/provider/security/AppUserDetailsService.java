package co.arctern.api.provider.security;

import co.arctern.api.provider.dao.UserDao;
import co.arctern.api.provider.domain.User;
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
import java.util.stream.Collectors;

/**
 * Setting details in the User security model through user entity
 */
@Component
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userDao.findByPhone(phone)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException(String.format("The contact %phone doesn't exist", phone));
                });
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        });
        co.arctern.api.provider.security.model.User userDetails = new co.arctern.api.provider.security.model.User(user.getUsername(), user.getPassword(), authorities);
        userDetails.setId(user.getId());
        userDetails.setName(user.getName());
        userDetails.setEmail(user.getEmail());
        userDetails.setPhone(user.getPhone());
        userDetails.setAreaIds(StringUtils.join(",", user.getUserAreas().stream().map(a -> a.getArea().getId()).collect(Collectors.toList())));
        return userDetails;
    }
}
