package co.arctern.rider.api.service;

import co.arctern.rider.api.domain.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    /**
     * to generate sign in
     *
     * @param phone
     * @param username
     * @param password
     * @return
     */
    public String signIn(String phone, String username, String password);

    /**
     * to sign up using user details.
     *
     * @param user
     * @return
     */
    public String signUp(User user);

    /**
     * to sign up in the system using phone number.
     *
     * @param user
     * @return
     */
    public String signUpByPhone(User user);

    /**
     * to delete user using username.
     *
     * @param username
     */
    public void deleteUser(String username);

    /**
     * to mark user as active/inactive using username.
     *
     * @param username
     * @param status
     * @return
     */
    public String markUserInactive(String username, Boolean status);

    /**
     * to fetch user using username.
     *
     * @param username
     * @return
     */
    public User fetchUser(String username);

    /**
     * to fetch user using phone.
     *
     * @param phone
     * @return
     */
    public User fetchUserByPhone(String phone);

}
