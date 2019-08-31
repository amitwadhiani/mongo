package co.arctern.rider.api.controller;

import co.arctern.rider.api.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@BasePathAwareController
public class RatingController {

    @Autowired
    RatingService ratingService;

    /**
     * to generate rating for a particular task.
     *
     * @param taskId
     * @param otp
     * @return
     */
    @GetMapping("/rating/generate")
    @CrossOrigin
    public ResponseEntity<String> generateRating(@RequestParam("taskId") Long taskId, @RequestParam("otp") String otp) {
        return ResponseEntity.ok(ratingService.postRatingForTask(taskId, otp));
    }
}
