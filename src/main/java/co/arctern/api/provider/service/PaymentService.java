package co.arctern.api.provider.service;

import co.arctern.api.provider.domain.Payment;
import co.arctern.api.provider.domain.Task;
import co.arctern.api.provider.dto.request.TaskAssignDto;
import co.arctern.api.provider.util.MessageUtil;

public interface PaymentService extends MessageUtil {

    /**
     * create payment for a task.
     * @param task
     * @param dto
     * @return
     */
    Payment create(Task task, TaskAssignDto dto);

    Payment patch(Task task);
}
