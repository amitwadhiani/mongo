package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.RoleDao;
import co.arctern.api.provider.dao.UserRoleDao;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserRole;
import co.arctern.api.provider.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleDao userRoleDao;
    private final RoleDao roleDao;

    @Autowired
    public UserRoleServiceImpl(UserRoleDao userRoleDao,
                               RoleDao roleDao) {
        this.userRoleDao = userRoleDao;
        this.roleDao = roleDao;
    }

    @Override
    public void createUserRoles(User user, List<Long> roleIds) {
        List<UserRole> userRoles = new ArrayList<>();
        List<UserRole> existingUserRoles = user.getUserRoles();
        if (!CollectionUtils.isEmpty(userRoles)) {
            existingUserRoles.stream().forEach(userRole -> userRole.setIsActive(false));
            userRoleDao.saveAll(existingUserRoles);
        }
        roleDao.findByIdIn(roleIds)
                .forEach(role -> {
                    UserRole userRole = new UserRole();
                    userRole.setRole(role);
                    userRole.setIsActive(true);
                    userRole.setUser(user);
                    userRoles.add(userRole);
                });
        userRoleDao.saveAll(userRoles);
    }

}
