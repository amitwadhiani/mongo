package co.arctern.api.provider.security;

import co.arctern.api.provider.dao.UserDao;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Setting details in the User security model through user entity
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException(String.format("The User %username doesn't exist", username));
                });
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getUserRoles().stream().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole().getRole()));
        });
        co.arctern.api.provider.security.model.User userDetails = new co.arctern.api.provider.security.model.User(user.getUsername(), user.getPassword(), authorities);
        userDetails.setId(user.getId());
        userDetails.setName(user.getName());
        userDetails.setEmail(user.getEmail());
        userDetails.setUsername(user.getUsername());
        userDetails.setPhone(user.getPhone());
        List<UserArea> userAreas = user.getUserAreas().stream().filter(a -> a.getArea().getCluster() != null).collect(Collectors.toList());
        /**
         * given that only one clusterId allowed per CLUSTER_MANAGER
         */
        if (!CollectionUtils.isEmpty(userAreas)) {
            userDetails.setClusterId(userAreas.get(0).getArea().getCluster().getId());
        }
        return userDetails;
    }
}
