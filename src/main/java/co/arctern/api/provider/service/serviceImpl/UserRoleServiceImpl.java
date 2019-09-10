package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.RoleDao;
import co.arctern.api.provider.dao.UserRoleDao;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserRole;
import co.arctern.api.provider.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public void createUserRoles(User user, List<Long> roleIds) {
        List<UserRole> userRoles = new ArrayList<>();
        List<UserRole> existingUserRoles = user.getUserRoles();
        existingUserRoles.stream().forEach(a -> a.setIsActive(false));
        userRoleDao.saveAll(existingUserRoles);
        roleDao.findByIdIn(roleIds)
                .forEach(a -> {
                    UserRole userRole = new UserRole();
                    userRole.setRole(a);
                    userRole.setIsActive(true);
                    userRole.setUser(user);
                    userRoles.add(userRole);
                });
        userRoleDao.saveAll(userRoles);
    }

}
