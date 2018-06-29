package cloud.cinder.common.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(RabbitTemplate.class)
@Slf4j
public class QueueSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(final String queue, String message) {
        this.rabbitTemplate.convertAndSend(queue, message);
    }
}