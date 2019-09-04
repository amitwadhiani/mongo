package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Rating;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.util.MessageUtil;

public interface RatingService extends MessageUtil {

    /**
     * save rating value for a Task.
     *
     * @param taskId
     * @param otp
     * @return
     */
    public String saveRating(Long taskId, String otp);

    /**
     * creating rating object for a task.
     *
     * @param task
     * @param otpNo
     * @param otpYes
     * @return
     */
    public Rating createRating(Task task, String otpNo, String otpYes);
}
