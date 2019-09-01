package co.arctern.api.provider.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * encoding util for password encoding and matching.
 */
@Service
public class EncodingUtil {

    @Autowired
    PasswordEncoder passwordEncoder;

    public String encodeString(String input) {
        return passwordEncoder.encode(input);
    }

    public Boolean matchPassword(String input, String original) {
        return passwordEncoder.matches(input, original);
    }

}
