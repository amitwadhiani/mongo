package co.arctern.api.provider.service;

import co.arctern.api.provider.constant.SettleState;
import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.dto.response.projection.Payments;
import co.arctern.api.provider.util.MessageUtil;

import java.util.List;

public interface PaymentService extends MessageUtil {

    /**
     * create payment for a task.
     *
     * @param task
     * @param dto
     * @return
     */
    Payment create(Task task, TaskAssignDto dto);

    /**
     * patch payment at task completion.
     *
     * @param task
     * @return
     */
    Payment patch(Task task);

    List<Payments> fetchSettleRequests(SettleState settleState);
}
