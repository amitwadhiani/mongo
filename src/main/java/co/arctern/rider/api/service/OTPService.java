package co.arctern.rider.api.service;

public interface OTPService {

    public String generateOTP(String phone);

    public String verifyOTP(String phone, String otp);
}
