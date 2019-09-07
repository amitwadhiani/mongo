package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.UserDao;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.request.UserRequestDto;
import co.arctern.api.provider.service.AreaService;
import co.arctern.api.provider.service.OfferingService;
import co.arctern.api.provider.service.UserRoleService;
import co.arctern.api.provider.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private OfferingService offeringService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public String markUserInactive(String username, Boolean status) {
        Optional<User> userOptional = userDao.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsActive(status);
            userDao.save(user);
            return "Success";
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND_MESSAGE);
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
    public User createUser(UserRequestDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setIsActive(true);
        user.setName(dto.getName());
        user.setIsTest(false);
        user.setAge(dto.getAge());
        user.setGender(dto.getGender());
        user.setIsLoggedIn(false);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setUsername(dto.getUsername());
        user = userDao.save(user);
        userRoleService.createUserRoles(user, dto.getRoleIds());
        areaService.setAreasToUser(user, dto.getAreaIds());
        offeringService.setOfferingsToUser(user, dto.getOfferingIds());
        return user;
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


}
