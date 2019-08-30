package co.arctern.rider.api.service;

import co.arctern.rider.api.domain.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    public String signIn(String phone, String username, String password);

    public String signUp(User user);

    public String signUpByPhone(User user);

    public void deleteUser(String username);

    public String markUserInactive(String username, Boolean status);

    public User fetchUser(HttpServletRequest req);

    public String refresh(String username);
}
