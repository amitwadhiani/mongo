package co.arctern.rider.api.service.serviceImpl;

import co.arctern.rider.api.dao.RatingDao;
import co.arctern.rider.api.dao.TaskDao;
import co.arctern.rider.api.domain.Rating;
import co.arctern.rider.api.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    RatingDao ratingDao;

    @Autowired
    TaskDao taskDao;

    @Override
    public String postRatingForTask(Long taskId, String otp) {
        Rating rating = new Rating();
        rating.setTask(taskDao.findById(taskId).get());
        if (otp.contains("s")) {
            rating.setIsSatisfied(true);
        } else {
            rating.setIsSatisfied(false);
        }
        ratingDao.save(rating);
        return "Success";
    }
}
