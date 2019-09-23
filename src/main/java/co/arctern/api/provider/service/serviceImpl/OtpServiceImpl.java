package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.service.*;
import co.arctern.api.provider.sms.SmsService;
import co.arctern.api.provider.util.OTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * otp Service to generate / use otp for various use cases in the api server.
 */
@Service
public class OtpServiceImpl implements OtpService {

    private final SmsService smsService;
    private final RatingService ratingService;
    private final TaskService taskService;

    @Autowired
    public OtpServiceImpl(SmsService smsService,
                          RatingService ratingService,
                          TaskService taskService) {
        this.smsService = smsService;
        this.ratingService = ratingService;
        this.taskService = taskService;
    }

    @Transactional
    @Override
    public StringBuilder generateOTPForRating(Long taskId) {
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
        if (smsService.sendSmsForRating(task.getPatientPhone(), getOtpString(), getOtpString()) != null) {
            return SUCCESS_MESSAGE;
        }
        return TRY_AGAIN_MESSAGE;
    }

    @Override
    public String getOtpString() {
        return OTPUtil.generateOtp().toString();
    }

}
