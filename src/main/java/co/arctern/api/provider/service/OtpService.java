package co.arctern.api.provider.service;

import co.arctern.api.provider.util.MessageUtil;

public interface OtpService extends MessageUtil {

    /**
     * generating otp for login.
     *
     * @param phone
     * @return
     */
    public String generateOTPForLogin(String phone);

    /**
     * generating otp for rating flow.
     *
     * @param taskId
     * @return
     */
    public StringBuilder generateOTPForRating(Long taskId);

    /**
     * fetch otp string.
     *
     * @return
     */
    public String getOtpString();

}
