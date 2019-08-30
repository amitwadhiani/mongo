package co.arctern.rider.api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncodingUtil {

    @Autowired
    PasswordEncoder passwordEncoder;

    public String encodeString(String input) {
        return passwordEncoder.encode(input);
    }

    public Boolean decodeString(String input, String original) {
        return passwordEncoder.matches(input, original);
    }

}
