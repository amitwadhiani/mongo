package co.arctern.api.provider.service.serviceImpl;

import co.arctern.api.provider.dao.RatingDao;
import co.arctern.api.provider.dao.TaskDao;
import co.arctern.api.provider.domain.Rating;
import co.arctern.api.provider.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingDao ratingDao;

    @Autowired
    private TaskDao taskDao;

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
