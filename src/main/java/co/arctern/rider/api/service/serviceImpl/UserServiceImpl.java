package co.arctern.rider.api.service.serviceImpl;

import co.arctern.rider.api.dao.UserDao;
import co.arctern.rider.api.domain.User;
import co.arctern.rider.api.dto.request.UserRequestDto;
import co.arctern.rider.api.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

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
    public User fetchUser(String username) {
        return userDao.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found."));
    }

    public User createUser(UserRequestDto userDto) {
        return null;
    }
}
