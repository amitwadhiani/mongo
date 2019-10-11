package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Role;
import co.arctern.api.provider.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRoleService {

    /**
     * map user with different roles.
     *
     * @param user
     * @param roleIds
     */
    public List<Role> createUserRoles(User user, List<Long> roleIds);
}
