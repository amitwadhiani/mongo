//package co.arctern.rider.api.service.serviceImpl;
//
//import javax.servlet.http.HttpServletRequest;
//
//import co.arctern.rider.api.dao.UserDaoImpl;
//import co.arctern.rider.api.domain.AppUser;
//import co.arctern.rider.api.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.Optional;
//
//
//@Service
//public class UserServiceImpl implements UserService {
//
//    @Autowired
//    private UserDaoImpl userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    public String signIn(String phone, String username, String password) {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
//        } catch (AuthenticationException e) {
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid username/password");
//        }
//    }
//
//    public String signUp(AppUser user) {
//        if (!userRepository.existsByUsername(user.getUsername())) {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//            userRepository.save(user);
//            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username unavailable");
//        }
//    }
//
//    public String signUpByPhone(AppUser user) {
//        if (!userRepository.existsByUsername(user.getUsername())) {
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//            userRepository.save(user);
//            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
//        } else {
//            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username unavailable");
//        }
//    }
//
//    public void deleteUser(String username) {
//        userRepository.deleteByUsername(username);
//    }
//
//    public String markUserInactive(String username, Boolean status) {
//        Optional<AppUser> userObject = userRepository.findByUsername(username);
//        if (!userObject.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username");
//        AppUser user = userObject.get();
//        user.setIsActive(status);
//        userRepository.save(user);
//        return "User: " + user.getId().toString() + "status changed." +;
//    }
//
//    public AppUser fetchByUsername(String username) {
//        AppUser user = userRepository.findByUsername(username).get();
//        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username");
//        return user;
//    }
//
//    public AppUser fetchUser(HttpServletRequest req) {
//        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
//    }
//
//    public String refresh(String username) {
//        return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
//    }
//
//}
