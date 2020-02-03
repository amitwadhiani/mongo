package co.arctern.api.provider.controller;

import co.arctern.api.provider.service.*;
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

    private final OtpService otpService;
    private final RatingService ratingService;
    private final TokenService tokenService;
    private final PaymentService paymentService;
    private final TaskService taskService;

    @Autowired
    public RatingController(OtpService otpService,
                            RatingService ratingService,
                            TokenService tokenService,
                            PaymentService paymentService,
                            TaskService taskService) {
        this.otpService = otpService;
        this.ratingService = ratingService;
        this.tokenService = tokenService;
        this.paymentService = paymentService;
        this.taskService = taskService;
    }

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
                                             @RequestParam(value = "userId", required = false) Long userId,
                                             @RequestParam(value = "amount", required = false) Double amount,
                                             @RequestParam(value = "mode", required = false) String mode) {
        if (userId == null) userId = tokenService.fetchUserId();
        if (amount != null) paymentService.updateAmount(taskService.fetchTask(taskId), amount);
        return ResponseEntity.ok(ratingService.saveRating(taskId, userId, otp, mode));
    }
}
