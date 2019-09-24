package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.constant.Gender;
import co.arctern.api.provider.dao.UserDao;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.domain.UserOffering;
import co.arctern.api.provider.dto.request.UserRequestDto;
import co.arctern.api.provider.dto.response.PaginatedResponse;
import co.arctern.api.provider.dto.response.projection.Users;
import co.arctern.api.provider.service.*;
import co.arctern.api.provider.util.PaginationUtil;
import lombok.SneakyThrows;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ProjectionFactory projectionFactory;
    private final OfferingService offeringService;
    private final AreaService areaService;
    private final UserRoleService userRoleService;
    private final TokenService tokenService;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           ProjectionFactory projectionFactory,
                           OfferingService offeringService,
                           AreaService areaService,
                           UserRoleService userRoleService,
                           TokenService tokenService) {
        this.userDao = userDao;
        this.projectionFactory = projectionFactory;
        this.offeringService = offeringService;
        this.areaService = areaService;
        this.userRoleService = userRoleService;
        this.tokenService = tokenService;
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
        return userDao.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, REGISTER_USER_MESSAGE.toString()));
    }

    @Override
    public User fetchUser(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND_MESSAGE.toString()));
    }

    @Override
    @SneakyThrows({HttpClientErrorException.BadRequest.class})
    public User fetchUserByPhone(String phone) {
        return userDao.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND_MESSAGE.toString()));
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    @Transactional
    public StringBuilder createUser(UserRequestDto dto) {
        Long userId = dto.getUserId();
        List<Long> roleIds = dto.getRoleIds();
        List<Long> areaIds = dto.getAreaIds();
        List<Long> offeringIds = dto.getOfferingIds();
        User user = (userId == null) ? new User() : userDao.findById(userId).get();
        Gender gender = dto.getGender();
        Date dateOfBirth = dto.getDateOfBirth();
        String name = dto.getName();
        Integer age = dto.getAge();
        String phone = dto.getPhone();
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
            user.setUsername(dto.getUsername());
            user.setEmail(dto.getEmail());
        }
        if (phone != null) user.setPhone(phone);
        user = userDao.save(user);
        if (!org.springframework.util.CollectionUtils.isEmpty(roleIds)) userRoleService.createUserRoles(user, roleIds);
        if (!org.springframework.util.CollectionUtils.isEmpty(areaIds)) areaService.setAreasToUser(user, areaIds);
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
    public List<Users> fetchAll() {
        List<Users> users = new ArrayList<>();
        userDao.findAll().forEach(a -> users.add(projectionFactory.createProjection(Users.class, a)));
        return users;
    }

    @Override
    public Page<User> fetchUsersByOffering(List<Long> offeringIds, Pageable pageable) {
        return offeringService.fetchUserOfferings(offeringIds, pageable).map(UserOffering::getUser);
    }

    public PaginatedResponse fetchAllUsersByAdmin(Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userDao.findByIsActiveTrue(pageable)
                .map(a -> projectionFactory.createProjection(Users.class, a)), pageable);
    }
}
