package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRoleService {

    public void createUserRoles(User user, List<Long> roleIds);
}
