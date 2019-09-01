package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.domain.Rating;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.service.*;
import co.arctern.api.provider.sms.SmsService;
import co.arctern.api.provider.util.OTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

/**
 * otp Service to generate / use otp for various use cases in the api server.
 */
@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private TaskService taskService;

    @Transactional
    public String generateOTPForLogin(String phone) {
        User user = userService.fetchUser(phone);
        String otp = getOtpString();
        if (smsService.sendSms(phone, otp) != null) {
            loginService.generateLogin(phone, otp, user);
        }
        return otp;
    }

    @Transactional
    public String generateOTPForRating(Long taskId) {
        Task task = taskService.fetchTask(taskId);
        String otpYes = this.getOtpString();
        String otpNo = this.getOtpString();
        /**
         * to handle equal otp values for both cases
         */
        while (otpYes.equals(otpNo)) {
            otpNo = this.getOtpString();
        }
        ratingService.createRating(task, otpNo, otpYes);
        if (smsService.sendSmsForRating(task.getUser().getPhone(), getOtpString(), getOtpString()) != null) {
            return "Success";
        }
        return "Please try again";
    }

    public String getOtpString() {
        return OTPUtil.generateOtp();
    }

}
