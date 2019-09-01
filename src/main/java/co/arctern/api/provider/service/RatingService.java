package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Rating;
import co.arctern.api.provider.domain.Task;

public interface RatingService {

    public String saveRating(Long taskId,String otp);

    public Rating createRating(Task task,String otpNo,String otpYes);
}
