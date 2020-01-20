package co.arctern.api.provider.eventhandler;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * handler for changes in user entity after login/logout .
 */
@Service
public class LoginEventHandler {

    private final UserService userService;

    @Autowired
    public LoginEventHandler(UserService userService) {
        this.userService = userService;
    }

    public void markLoggedInStateForUser(User user, Boolean status, Timestamp lastLoginTime) {
        user.setIsLoggedIn(status);
        user.setLastLoginTime(lastLoginTime);
        user.setIsLoggedIn(true);
        userService.save(user);
    }
}
