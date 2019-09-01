package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.RatingDao;
import co.arctern.api.provider.domain.Rating;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.service.RatingService;
import co.arctern.api.provider.service.TaskService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingDao ratingDao;

    @Autowired
    private TaskService taskService;

    @Override
    @SneakyThrows(Exception.class)
    public String saveRating(Long taskId, String otp) {
        Task task = taskService.fetchTask(taskId);
        Rating rating = task.getRating();
        if (otp.equals(rating.getOtpNo())) {
            rating.setIsSatisfied(false);
        } else if (otp.equals(rating.getOtpYes())) {
            rating.setIsSatisfied(true);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid otp entered.");
        }
        rating.setTask(task);
        ratingDao.save(rating);
        return "Success";
    }

    public Rating createRating(Task task, String otpNo, String otpYes) {
        Rating rating = new Rating();
        rating.setOtpNo(otpNo);
        rating.setOtpYes(otpYes);
        rating.setTask(task);
        return ratingDao.save(rating);
    }
}