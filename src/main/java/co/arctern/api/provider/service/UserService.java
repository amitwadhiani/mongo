package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.request.UserRequestDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.UserResponseForPatientApp;
import co.arctern.api.provider.dto.response.projection.Users;
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
     * update user profile.
     *
     * @param dto
     * @return
     */
    public StringBuilder updateUser(UserRequestDto dto);

    /**
     * save last login time based on login.
     *
     * @param phone
     * @param loginTime
     */
    public void saveLastLoginTime(String phone, Timestamp loginTime);

    /**
     * fetch all users who don't have admin role.
     *
     * @return
     */
    public PaginatedResponse fetchAll(Pageable pageable);

    /**
     * fetch providers by type and cluster.
     *
     * @param type
     * @param clusterId
     * @param pageable
     * @return
     */
    public PaginatedResponse fetchAllByTaskTypeAndCluster(TaskType type, Long clusterId, Pageable pageable);

    /**
     * fetch providers by cluster.
     *
     * @param clusterId
     * @param pageable
     * @return
     */
    public PaginatedResponse fetchAllByCluster(Long clusterId, Pageable pageable);

    /**
     * fetch all providers by taskType.
     *
     * @param taskType
     * @param pageable
     * @return
     */
    public PaginatedResponse fetchAllByTaskType(TaskType taskType, Pageable pageable);

    /**
     * fetch user details through taskId.
     *
     * @return
     */
    public Users fetchDetails(Long taskId);

    /**
     * fetch all users.
     *
     * @return
     */
    public PaginatedResponse fetchActiveUsersByAdmin(Pageable pageable);

    /**
     * fetch all users.
     *
     * @return
     */
    public PaginatedResponse fetchAllUsersByAdmin(Pageable pageable);

    /**
     * fetch users filtered by offerings.
     *
     * @param offeringIds
     * @param pageable
     * @return
     */
    public Page<User> fetchUsersByOffering(List<Long> offeringIds, Pageable pageable);

    /**
     * search users.
     *
     * @param value
     * @param pageable
     * @return
     */
    public Page<Users> search(String value, Pageable pageable);

    /**
     * activate/deactivate provider.
     *
     * @param userId
     * @param isActive
     * @return
     */
    public StringBuilder activateOrDeactivateUser(Long userId, Boolean isActive);

    /**
     * fetch providers by pinCode.
     *
     * @param pincode
     * @return
     */
    public Integer fetchUserByPincode(String pincode);

    public UserResponseForPatientApp fetchUserByTaskId(Long taskId);

}
