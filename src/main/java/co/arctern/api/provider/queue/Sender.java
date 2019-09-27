package co.arctern.api.provider.queue;

import co.arctern.api.provider.domain.User;
import co.arctern.api.provider.queue.notification.ProviderAssignTaskEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.order-event-name:order-notification-updates-simple}")
    public String orderNotificationQueueName;

    public void sendAdminAssignTaskNotification(User user) throws JsonProcessingException {
        String object = new ObjectMapper().writeValueAsString(new ProviderAssignTaskEvent(user));
        log.info("Sending message for Assign task : " + object);
        this.rabbitTemplate.convertAndSend(orderNotificationQueueName, object);
    }

}

