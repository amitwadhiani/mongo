package co.arctern.api.provider.queue.notification;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TaskEvent for event -> Task state change event.
 */
@Data
@NoArgsConstructor
public class ProviderTaskStateChangeEvent implements Serializable {

    private String providerPhone;
    private Long providerId;
    private String providerName;
    private String messageQueueState;
    private Long patientId;

    public ProviderTaskStateChangeEvent(User user, TaskState taskState, Long patientId) {
        this.providerPhone = user.getPhone();
        this.providerId = user.getId();
        this.providerName = user.getName();
        this.patientId = patientId;

        if (taskState.equals(TaskState.ASSIGNED))
            this.messageQueueState = "ADMIN_ASSIGN_TASK";

        if (taskState.equals(TaskState.STARTED))
            this.messageQueueState = "PROVIDER_START_TASK";

        if (taskState.equals(TaskState.ACCEPTED))
            this.messageQueueState = "PROVIDER_ACCEPTED_TASK";

        if (taskState.equals(TaskState.CANCELLED))
            this.messageQueueState = "PROVIDER_CANCELLED_TASK";

        if (taskState.equals(TaskState.REJECTED))
            this.messageQueueState = "PROVIDER_REJECTED_TASK";

        if (taskState.equals(TaskState.RESCHEDULED))
            this.messageQueueState = "PROVIDER_RESCHEDULED_TASK";

        if (taskState.equals(TaskState.COMPLETED))
            this.messageQueueState = "PROVIDER_COMPLETED_TASK";

    }
}
