package co.arctern.api.provider.controller;

import co.arctern.api.provider.domain.Rating;
import co.arctern.api.provider.service.OtpService;
import co.arctern.api.provider.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@BasePathAwareController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private RatingService ratingService;

    /**
     * to generate rating for a particular task.
     *
     * @param taskId
     * @return
     */
    @GetMapping("/create")
    @CrossOrigin
    public ResponseEntity<StringBuilder> generateRating(@RequestParam("taskId") Long taskId) {
        return ResponseEntity.ok(otpService.generateOTPForRating(taskId));
    }

    @GetMapping("/save")
    @CrossOrigin
    public ResponseEntity<String> saveRating(@RequestParam("taskId") Long taskId, @RequestParam("otp") String otp) {
        return ResponseEntity.ok(ratingService.saveRating(taskId, otp));
    }
}
