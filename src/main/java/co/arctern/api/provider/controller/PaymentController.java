package co.arctern.api.provider.controller;

import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.service.GenericService;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/payment")
@BasePathAwareController
public class PaymentController {

    private final TaskService taskService;
    private final TokenService tokenService;
    private final GenericService genericService;

    @Autowired
    public PaymentController(TaskService taskService,
                             TokenService tokenService,
                             GenericService genericService) {
        this.taskService = taskService;
        this.tokenService = tokenService;
        this.genericService = genericService;
    }

    /**
     * request settlement api.
     *
     * @param settleState
     * @return
     */
    @CrossOrigin
    @PostMapping("/request")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Payments>> requestSettlement(@RequestParam(value = "settleState", required = false,
            defaultValue = "REQUESTED") SettleState settleState) {
        return ResponseEntity.ok(taskService.requestSettlement(tokenService.fetchUserId(), settleState));
    }

    /**
     * view payment settle requests.
     *
     * @param settleState
     * @return
     */
    @CrossOrigin
    @PostMapping("/settle/view")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Payments>> settleOrView(@RequestParam(value = "settleState", required = false,
            defaultValue = "PAYMENT_RECEIVED") SettleState settleState) {
        return ResponseEntity.ok(taskService.settle(tokenService.fetchUserId(), settleState));
    }

    /**
     * settle payment requests.
     *
     * @param userId
     * @return
     */
    @CrossOrigin
    @PostMapping("/settle")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<List<Payments>> settle(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok(taskService.settleAmountForProvider(tokenService.fetchUserId(), userId));
    }

    /**
     * fetch user owed amount api.
     *
     * @param userId
     * @return
     */
    @CrossOrigin
    @GetMapping("/user/owed")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','ROLE_CLUSTER_MANAGER')")
    public ResponseEntity<Double> fetchUserOwedAmount(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok(genericService.fetchUserOwedAmount(userId));
    }


}
