package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.util.MessageUtil;

public interface UserService  extends MessageUtil {

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
     * @param phone
     * @return
     */
    public User fetchUser(String phone);

    /**
     * to fetch user using username.
     *
     * @param userId
     * @return
     */
    public User fetchUser(Long userId);

    /**
     * to fetch user using phone.
     *
     * @param phone
     * @return
     */
    public User fetchUserByPhone(String phone);

    public User save(User user);

}
