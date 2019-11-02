package co.arctern.api.provider.controller;

import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.service.TaskService;
import co.arctern.api.provider.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/payment")
@BasePathAwareController
public class PaymentController {

    private final TaskService taskService;
    private final TokenService tokenService;

    @Autowired
    public PaymentController(TaskService taskService,
                             TokenService tokenService) {
        this.taskService = taskService;
        this.tokenService = tokenService;
    }

    @CrossOrigin
    @PostMapping("/request")
    public ResponseEntity<List<Payments>> requestSettlement(@RequestParam(value = "settleState", required = false,
            defaultValue = "PAYMENT_RECEIVED") SettleState settleState) {
        return ResponseEntity.ok(taskService.requestSettlement(tokenService.fetchUserId(), settleState));
    }

    @CrossOrigin
    @PostMapping("/settle/view")
    public ResponseEntity<List<Payments>> settleOrView(@RequestParam(value = "settleState", required = false,
            defaultValue = "PAYMENT_RECEIVED") SettleState settleState) {
        return ResponseEntity.ok(taskService.settle(tokenService.fetchUserId(), settleState));
    }

    @CrossOrigin
    @PostMapping("/settle")
    public ResponseEntity<List<Payments>> settle(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok(taskService.settleAmountForProvider(tokenService.fetchUserId(), userId));
    }

}
