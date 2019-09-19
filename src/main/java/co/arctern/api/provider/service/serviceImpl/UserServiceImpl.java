package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.UserDao;
import co.arctern.api.provider.domain.User;
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
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProjectionFactory projectionFactory;

    @Autowired
    private OfferingService offeringService;

    @Autowired
    private UserOfferingService userOfferingService;

    @Autowired
    private AreaService areaService;

    @Autowired
    UserAreaService userAreaService;

    @Autowired
    private UserRoleService userRoleService;

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
        User user = fetchUser(userId);
        user.setIsActive(status);
        userDao.save(user);
        return SUCCESS_MESSAGE;
    }

    @Override
    @SneakyThrows({HttpClientErrorException.BadRequest.class})
    public User fetchUser(String phone) {
        return userDao.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, REGISTER_USER_MESSAGE));
    }

    @Override
    public User fetchUser(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND_MESSAGE));
    }

    @Override
    @SneakyThrows({HttpClientErrorException.BadRequest.class})
    public User fetchUserByPhone(String phone) {
        return userDao.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND_MESSAGE));
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
        user.setEmail(dto.getEmail());
        user.setIsActive(true);
        user.setName(dto.getName());
        user.setIsTest(false);
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setIsLoggedIn(false);
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setUsername(dto.getUsername());
        user = userDao.save(user);
        if (!org.springframework.util.CollectionUtils.isEmpty(roleIds)) userRoleService.createUserRoles(user, roleIds);
        if (!org.springframework.util.CollectionUtils.isEmpty(areaIds)) areaService.setAreasToUser(user, areaIds);
        if (!CollectionUtils.isEmpty(offeringIds)) offeringService.setOfferingsToUser(user, offeringIds);
        return SUCCESS_MESSAGE;
    }

    @Transactional
    @Override
    public void saveLastLoginTime(String phone, Timestamp loginTime) {
        User user = userDao.findByPhone(phone).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    USER_NOT_FOUND_MESSAGE);
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
        return offeringService.fetchUserOfferings(offeringIds, pageable).map(a -> a.getUser());
    }

    public PaginatedResponse fetchAllUsersByAdmin(Pageable pageable) {
        return PaginationUtil.returnPaginatedBody(userDao.findByIsActiveTrue(pageable)
                .map(a -> projectionFactory.createProjection(Users.class, a)), pageable);
    }
}
