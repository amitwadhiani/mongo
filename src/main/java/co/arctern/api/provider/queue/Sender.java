package co.arctern.api.provider.queue;

import co.arctern.api.provider.constant.TaskState;
import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.queue.notification.ProviderAssignTaskEvent;
import co.arctern.api.provider.queue.notification.ProviderTaskStateChangeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * sender for push notifications.
 */
@Component
@Slf4j
public class Sender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${spring.rabbitmq.order-event-name:order-notification-updates-simple}")
    public String orderNotificationQueueName;

    /**
     * to send notification for assigned task by admin to the user.
     *
     * @param user
     * @throws JsonProcessingException
     */
    public void sendAdminAssignTaskNotification(User user, Long patientId, Long providerTaskId) throws JsonProcessingException {
        String object = new ObjectMapper().writeValueAsString(new ProviderAssignTaskEvent(user,patientId, providerTaskId));
        log.info("Sending message for Assigned tasks : " + object);
        this.rabbitTemplate.convertAndSend(orderNotificationQueueName, object);
    }

    public void sendTaskStateChangeNotification(User user, TaskState taskState, Long patientId, Long providerTaskId) throws JsonProcessingException {
        String object = new ObjectMapper().writeValueAsString(new ProviderTaskStateChangeEvent(user, taskState, patientId, providerTaskId ));
        log.info("Sending message for task state change : " + object);
        this.rabbitTemplate.convertAndSend(orderNotificationQueueName, object);
    }

}

