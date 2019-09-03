package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.UserDao;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.dto.request.UserRequestDto;
import co.arctern.api.provider.service.AreaService;
import co.arctern.api.provider.service.RoleService;
import co.arctern.api.provider.service.UserRoleService;
import co.arctern.api.provider.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    RoleService roleService;

    @Autowired
    AreaService areaService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    PasswordEncoder passwordEncoder;

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
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username.");
    }

    @Override
    @SneakyThrows({HttpClientErrorException.BadRequest.class})
    public User fetchUser(String phone) {
        return userDao.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found. Ask admin " +
                        "for new sign up. "));
    }

    @Override
    public User fetchUser(Long userId) {
        return userDao.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found."));
    }

    @Override
    @SneakyThrows({HttpClientErrorException.BadRequest.class})
    public User fetchUserByPhone(String phone) {
        return userDao.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found."));
    }

    public User save(User user) {
        return userDao.save(user);
    }

    public User createUser(UserRequestDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setIsActive(true);
        user.setName(dto.getName());
        user.setIsTest(false);
        user.setLoginState(false);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setUsername(dto.getUsername());
        user = userDao.save(user);
        userRoleService.createUserRoles(user, dto.getRoleIds());
        areaService.setAreasToUser(user, dto.getAreaIds());
        return user;
    }

}
