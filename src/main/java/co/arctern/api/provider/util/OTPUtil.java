package co.arctern.api.provider.util;

import java.util.Random;

public interface OTPUtil {

    public static final Integer OTP_LENGTH = 6;

    public static String generateOtp() {
        String pool = "0123456789";
        Random random = new Random();
        char[] chars = new char[OTP_LENGTH];
        int i = 0;
        while (i < 6) {
            chars[i] = pool.charAt(random.nextInt(pool.length()));
            i++;
        }
        return new String(chars);
    }
}
