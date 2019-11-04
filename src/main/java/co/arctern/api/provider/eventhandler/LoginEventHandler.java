package co.arctern.api.provider.eventhandler;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void markLoggedInStateForUser(User user, Boolean status) {
        user.setIsLoggedIn(status);
        userService.save(user);
    }
}
