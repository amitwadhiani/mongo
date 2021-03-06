package co.arctern.api.provider.queue.notification;

import co.arctern.api.provider.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TaskEvent for event -> admin assigns task to a provider.
 */
@Data
@NoArgsConstructor
public class ProviderAssignTaskEvent implements Serializable {

    private String providerPhone;
    private Long providerId;
    private String providerName;
    private String messageQueueState;
    private Long patientId;
    private Long providerTaskId;

    public ProviderAssignTaskEvent(User user, Long patientId, Long providerTaskId) {
        this.providerPhone = user.getPhone();
        this.providerId = user.getId();
        this.providerName = user.getName();
        this.messageQueueState = "ADMIN_ASSIGN_TASK";
        this.patientId = patientId;
        this.providerTaskId = providerTaskId;
    }
}
