package co.arctern.api.provider.util;

import java.util.Random;

/**
 * otp generation utility.
 */
public interface OTPUtil {

    /**
     * standard OTP length.
     */
    public static final Integer OTP_LENGTH = 4;

    /**
     * otp generation method.
     *
     * @return
     */
    public static StringBuilder generateOtp() {
        String pool = "0123456789";
        Random random = new Random();
        char[] chars = new char[OTP_LENGTH];
        int i = 0;
        StringBuilder otp = new StringBuilder("");
        while (i < OTP_LENGTH) {
            otp.append(pool.charAt(random.nextInt(pool.length())));
            i++;
        }
        return otp;
    }
}
