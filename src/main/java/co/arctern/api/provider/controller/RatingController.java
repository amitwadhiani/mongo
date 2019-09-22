package co.arctern.api.provider.controller;

import co.arctern.api.provider.service.OtpService;
import co.arctern.api.provider.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * apis for rating flow after task completion.
 */
@BasePathAwareController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private RatingService ratingService;

    /**
     * to generate rating for a particular task api.
     *
     * @param taskId
     * @return
     */
    @PostMapping("/create")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<StringBuilder> generateRating(@RequestParam("taskId") Long taskId) {
        return ResponseEntity.ok(otpService.generateOTPForRating(taskId));
    }

    /**
     * to save rating based on task_id and otp api.
     *
     * @param taskId
     * @param otp
     * @return
     */
    @PatchMapping("/save")
    @CrossOrigin
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> saveRating(@RequestParam("taskId") Long taskId,
                                             @RequestParam(value = "otp", required = false) String otp,
                                             @RequestParam("userId") Long userId) {
        return ResponseEntity.ok(ratingService.saveRating(taskId, userId, otp));
    }
}
