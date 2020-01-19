package co.arctern.api.provider.service.serviceimpl;

import co.arctern.api.provider.dao.RatingDao;
import co.arctern.api.provider.domain.Rating;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.service.PaymentService;
import co.arctern.api.provider.service.RatingService;
import co.arctern.api.provider.service.TaskService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;


@Service
public class RatingServiceImpl implements RatingService {

    private final RatingDao ratingDao;
    private final TaskService taskService;
    private final PaymentService paymentService;

    @Autowired
    public RatingServiceImpl(RatingDao ratingDao,
                             TaskService taskService,
                             PaymentService paymentService) {
        this.ratingDao = ratingDao;
        this.taskService = taskService;
        this.paymentService = paymentService;
    }

    @Override
    @SneakyThrows(Exception.class)
    @Transactional
    public String saveRating(Long taskId, Long userId, String otp) {
        Task task = taskService.fetchTask(taskId);
        Rating rating = task.getRatings().get(0);
        if (!StringUtils.isEmpty(otp)) {
            if (otp.equals(rating.getOtpNo())) {
                rating.setIsSatisfied(false);
            } else if (otp.equals(rating.getOtpYes())) {
                rating.setIsSatisfied(true);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_OTP_MESSAGE.toString());
            }
        }
        rating.setTask(task);
        ratingDao.save(rating);
        taskService.completeTask(taskId, userId);
        paymentService.patch(task, userId);
        return SUCCESS_MESSAGE.toString();
    }

    @Override
    @Transactional
    public Rating createRating(Task task, String otpNo, String otpYes) {
        Rating rating = new Rating();
        rating.setOtpNo(otpNo);
        rating.setOtpYes(otpYes);
        rating.setTask(task);
        return ratingDao.save(rating);
    }
}
