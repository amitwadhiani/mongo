package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.request.UserRequestDto;
import co.arctern.api.provider.dto.response.projection.Users;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface UserService extends MessageUtil {

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
     * to mark user as active/inactive using userId.
     *
     * @param userId
     * @param status
     * @return
     */
    public StringBuilder markUserInactive(Long userId, Boolean status);

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

    /**
     * save user.
     *
     * @param user
     * @return
     */
    public User save(User user);

    /**
     * new user creation.
     *
     * @param dto
     * @return
     */
    public StringBuilder createUser(UserRequestDto dto);

    /**
     * save last login time based on login.
     *
     * @param phone
     * @param loginTime
     */
    public void saveLastLoginTime(String phone, Timestamp loginTime);

    /**
     * fetch all users.
     * @return
     */
    public List<Users> fetchAll();

    /**
     * fetch users filtered by offerings.
     * @param offeringIds
     * @param pageable
     * @return
     */
    public Page<User> fetchUsersByOffering(List<Long> offeringIds, Pageable pageable);

    public Map<Object, List<Users>> fetchAllByAreaOrOffering(String type, Pageable pageable);
}
