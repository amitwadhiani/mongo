package co.arctern.api.provider.service;

import co.arctern.api.provider.util.MessageUtil;

public interface OtpService extends MessageUtil {

    public String generateOTPForLogin(String phone);

    public StringBuilder generateOTPForRating(Long taskId);

    public String getOtpString();

}
