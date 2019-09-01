package co.arctern.api.provider.service;

public interface OtpService {

    public String generateOTPForLogin(String phone);

    public String generateOTPForRating(Long taskId);

    public String getOtpString();

}
