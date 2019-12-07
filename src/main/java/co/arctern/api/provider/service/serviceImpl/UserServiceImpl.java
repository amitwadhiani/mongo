package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.Gender;
import co.arctern.api.provider.constant.TaskType;
import co.arctern.api.provider.dao.UserDao;
import co.arctern.api.provider.domain.*;
import co.arctern.api.provider.dto.request.UserRequestDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.Users;
import co.arctern.api.provider.service.*;
import co.arctern.api.provider.util.PaginationUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ProjectionFactory projectionFactory;
    private final OfferingService offeringService;
    private final AreaService areaService;
    private final ClusterService clusterService;
    private final UserRoleService userRoleService;
    private final TokenService tokenService;
    private final UserTaskService userTaskService;
    private final GenericService genericService;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           ProjectionFactory projectionFactory,
                           OfferingService offeringService,
                           AreaService areaService,
                           UserRoleService userRoleService,
                           TokenService tokenService,
                           UserTaskService userTaskService,
                           GenericService genericService,
                           ClusterService clusterService) {
        this.userDao = userDao;
        this.projectionFactory = projectionFactory;
        this.offeringService = offeringService;
        this.areaService = areaService;
        this.userRoleService = userRoleService;
        this.tokenService = tokenService;
        this.userTaskService = userTaskService;
        this.genericService = genericService;
        this.clusterService = clusterService;
    }

    @Override
    public String signIn(String phone, String username, String password) {
        return null;
    }

    @Override
    public String signUp(User user) {
        return null;
    }

    @Override
    public String signUpByPhone(User user) {
        return null;
    }

    @Override
    @Deprecated
    public void deleteUser(String username) {
        userDao.deleteByUsername(username);
    }

    @Override
    @SneakyThrows({HttpClientErrorException.BadRequest.class})
    public StringBuilder markUserInactive(Long userId, Boolean status) {
        Long id = (userId == null) ? tokenService.fetchUserId() : userId;
        User user = fetchUser(id);
        user.setIsActive(status);
        userDao.save(user);
        return SUCCESS_MESSAGE;
    }

    @Override
    @SneakyThrows({HttpClientErrorException.BadRequest.class})
    public User fetchUser(String phone) {
        return userDao.findByPhoneAndIsActiveTrue(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, REGISTER_USER_MESSAGE.toString()));
    }

    @Override
    public User fetchUser(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND_MESSAGE.toString()));
    }

    @Override
    @SneakyThrows({HttpClientErrorException.BadRequest.class})
    public User fetchUserByPhone(String phone) {
        return userDao.findByPhoneAndIsActiveTrue(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND_MESSAGE.toString()));
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    @Transactional
    public StringBuilder createUser(UserRequestDto dto) {
        String phone = dto.getPhone();
        String username = dto.getUsername();
        String email = dto.getEmail();
        if (userDao.existsByPhone(phone))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PHONE_ALREADY_EXISTS_MESSAGE.toString());
        if (userDao.existsByUsername(username))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USERNAME_ALREADY_EXISTS_MESSAGE.toString());
        if (userDao.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_EXISTS_MESSAGE.toString());
        Long userId = dto.getUserId();
        List<Long> roleIds = dto.getRoleIds();
        List<Long> areaIds = dto.getAreaIds();
        List<Long> offeringIds = dto.getOfferingIds();
        User user = (userId == null) ? new User() : userDao.findById(userId).get();
        Gender gender = dto.getGender();
        Date dateOfBirth = dto.getDateOfBirth();
        String name = dto.getName();
        Integer age = dto.getAge();
        Boolean isActive = dto.getIsActive();
        user.setIsActive((isActive == null) ? true : isActive);
        if (name != null) user.setName(name);
        user.setIsTest(false);
        if (dateOfBirth != null) user.setDateOfBirth(dateOfBirth);
        if (age != null) user.setAge(age);
        if (gender != null) user.setGender(gender);
        if (userId == null) {
            user.setIsLoggedIn(false);
            user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
            user.setUsername(username);
            user.setEmail(email);
        }
        if (phone != null) user.setPhone(phone);
        user = userDao.save(user);
        List<Role> roles = (!org.springframework.util.CollectionUtils.isEmpty(roleIds)) ?
                userRoleService.createUserRoles(user, roleIds) : user.getUserRoles().stream().map(a -> a.getRole()).collect(Collectors.toList());
        Long clusterId = dto.getClusterId();
        areaService.setAreasToUser(user, areaIds, roles, clusterId);
        if (!CollectionUtils.isEmpty(offeringIds)) offeringService.setOfferingsToUser(user, offeringIds);
        return SUCCESS_MESSAGE;
    }

    @Override
    public StringBuilder updateUser(UserRequestDto dto) {
        return this.createUser(dto);
    }

    @Transactional
    @Override
    public void saveLastLoginTime(String phone, Timestamp loginTime) {
        User user = userDao.findByPhone(phone).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    USER_NOT_FOUND_MESSAGE.toString());
        });
        user.setLastLoginTime(loginTime);
        user.setIsLoggedIn(true);
        userDao.save(user);
    }

    @Override
    public PaginatedResponse fetchAll(Pageable pageable) {
        List<User> users = userDao.fetchActiveUsers().stream()
                .filter(user -> !user.getUserRoles().stream().anyMatch(userRole -> userRole.getRole().getRole().equals("ROLE_ADMIN")))
                .collect(Collectors.toList());
        return PaginationUtil.returnPaginatedBody(
                users.stream()
                        .map(user -> {
                            user.setAmountOwed(genericService.fetchUserOwedAmount(user.getId()));
                            return projectionFactory.createProjection(Users.class, user);
                        }).collect(Collectors.toList()),
                pageable.getPageNumber(),
                pageable.getPageSize(), users.size());
    }

    @Override
    public PaginatedResponse fetchAllByTaskType(TaskType taskType, Pageable pageable) {
        List<User> users = userDao.fetchActiveUsers().stream()
                .filter(a -> !a.getUserRoles().stream()
                        .anyMatch(userRole -> userRole.getRole().getRole().equals("ROLE_ADMIN")) && a.getUserOfferings().stream().anyMatch(b -> b.getOffering().getType().toString().equalsIgnoreCase(taskType.toString())))
                .collect(Collectors.toList());
        return PaginationUtil.returnPaginatedBody(
                users.stream()
                        .map(a -> projectionFactory.createProjection(Users.class, a))
                        .collect(Collectors.toList()), pageable.getPageNumber(), pageable.getPageSize(), users.size());
    }

    /**
     * fetch user details through taskId.
     *
     * @param taskId
     * @return
     */
    @Override
    public Users fetchDetails(Long taskId) {
        UserTask activeUserTask = userTaskService.findActiveUserTask(taskId);
        if (activeUserTask == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_ACTIVE_USER_MESSAGE.toString());
        return projectionFactory.createProjection(Users.class, activeUserTask.getUser());
    }

    @Override
    public Page<User> fetchUsersByOffering(List<Long> offeringIds, Pageable pageable) {
        return offeringService.fetchUserOfferings(offeringIds, pageable).map(UserOffering::getUser);
    }

    @Override
    public PaginatedResponse fetchActiveUsersByAdmin(Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userDao.findByIsActiveTrue(pageable)
                .map(a -> projectionFactory.createProjection(Users.class, a)), pageable);
    }

    @Override
    public PaginatedResponse fetchAllUsersByAdmin(Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userDao.fetchUsers(pageable), pageable);
    }

    @Override
    public Page<Users> search(String value, Pageable pageable) {
        if (value.matches("^[0-9]*$")) {
            return userDao.findById(Long.valueOf(value), pageable)
                    .map(a -> projectionFactory.createProjection(Users.class, a));
        }
        return userDao.findByNameStartingWith(value, pageable)
                .map(a -> projectionFactory.createProjection(Users.class, a));
    }

    public PaginatedResponse fetchAllByTaskTypeAndCluster(TaskType type, Long clusterId, Pageable pageable) {
        List<String> pinCodes = clusterService.fetchAreas(clusterId).stream().map(a -> a.getPinCode()).collect(Collectors.toList());
        List<User> users = userDao.fetchActiveUsers().stream()
                .filter(a -> !a.getUserRoles().stream()
                        .anyMatch(userRole -> userRole.getRole().getRole().equals("ROLE_ADMIN"))
                        && a.getUserOfferings().stream().anyMatch(b -> b.getOffering().getType().toString().equalsIgnoreCase(type.toString()))
                        && a.getUserAreas()
                        .stream()
                        .filter(b -> BooleanUtils.isTrue(b.getIsActive()))
                        .anyMatch(b -> pinCodes.contains(b.getArea().getPinCode())))
                .collect(Collectors.toList());
        return PaginationUtil.returnPaginatedBody(
                users.stream()
                        .map(user -> {
                            user.setAmountOwed(genericService.fetchUserOwedAmount(user.getId()));
                            return projectionFactory.createProjection(Users.class, user);
                        }).collect(Collectors.toList()),
                pageable.getPageNumber(), pageable.getPageSize(), users.size());
    }

    public PaginatedResponse fetchAllByCluster(Long clusterId, Pageable pageable) {
        List<String> pinCodes = clusterService.fetchAreas(clusterId).stream().map(a -> a.getPinCode()).collect(Collectors.toList());
        List<User> users = userDao.fetchActiveUsers().stream()
                .filter(user -> !user.getUserRoles().stream().anyMatch(userRole -> userRole.getRole().getRole().equals("ROLE_ADMIN"))
                        && user.getUserAreas()
                        .stream()
                        .filter(a -> BooleanUtils.isTrue(a.getIsActive()))
                        .anyMatch(a -> pinCodes.contains(a.getArea().getPinCode())))
                .collect(Collectors.toList());
        return PaginationUtil.returnPaginatedBody(
                users.stream()
                        .map(user -> {
                            user.setAmountOwed(genericService.fetchUserOwedAmount(user.getId()));
                            return projectionFactory.createProjection(Users.class, user);
                        }).collect(Collectors.toList()),
                pageable.getPageNumber(),
                pageable.getPageSize(), users.size());
    }

    @Override
    public StringBuilder activateOrDeactivateUser(Long userId, Boolean isActive) {
        User user = this.fetchUser(userId);
        user.setIsActive(isActive);
        userDao.save(user);
        return SUCCESS_MESSAGE;
    }

    @Override
    public Integer fetchUserByPincode(String pincode) {
        Area area = areaService.fetchByPincode(pincode);
        if (area.getCluster() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No cluster found");
        }
        List<String> pinCodes = clusterService.fetchAreas(area.getCluster().getId()).stream().map(a -> a.getPinCode()).collect(Collectors.toList());

        return (userDao.fetchActiveUsers().stream()
                .filter(user -> !user.getUserRoles().stream().anyMatch(userRole -> userRole.getRole().getRole().equals("ROLE_ADMIN"))
                        && user.getUserAreas()
                        .stream()
                        .filter(a -> BooleanUtils.isTrue(a.getIsActive()))
                        .anyMatch(a -> pinCodes.contains(a.getArea().getPinCode())))
                .collect(Collectors.toList())).size();
    }
}
