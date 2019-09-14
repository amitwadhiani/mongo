package co.arctern.api.provider.security;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor
public class CustomPasswordEncoder extends BCryptPasswordEncoder {

    private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    private final int strength = -1;

    @Value("${security.jwt.client-secret}")
    private String secret;

    public CustomPasswordEncoder(int strength, SecureRandom random) {
        super(strength, random);
    }

    /**
     * Overriding the matches function of @BCryptPasswordEncoder.(For login By OTP)
     *
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword.toString().length() != secret.length()) {
            if (rawPassword.toString().equals(encodedPassword)) {
                return true;
            } else if (rawPassword.toString() != encodedPassword) {
                return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
            }
            return false;
        }
        if (encodedPassword == null || encodedPassword.length() == 0) {
            log.warn("Empty encoded password");
            return false;
        } else if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            log.warn("Encoded password does not look like BCrypt");
            return false;
        }
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
