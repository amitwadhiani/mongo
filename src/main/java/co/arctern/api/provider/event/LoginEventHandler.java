package co.arctern.api.provider.event;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginEventHandler {

    @Autowired
    UserService userService;

    public void markLoggedInStateForUser(User user, Boolean status) {
        user.setLoginState(status);
        userService.save(user);
    }


}
