package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.request.UserRequestDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.util.MessageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

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
    public User createUser(UserRequestDto dto);

    /**
     * save last login time based on login.
     *
     * @param phone
     * @param loginTime
     */
    public void saveLastLoginTime(String phone, Timestamp loginTime);

    public Page<User> fetchUsersByOffering(List<Long> offeringIds, Pageable pageable);
}
