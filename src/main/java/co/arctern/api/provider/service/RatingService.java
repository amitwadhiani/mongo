package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Rating;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.util.MessageUtil;

public interface RatingService  extends MessageUtil {

    public String saveRating(Long taskId,String otp);

    public Rating createRating(Task task,String otpNo,String otpYes);
}
