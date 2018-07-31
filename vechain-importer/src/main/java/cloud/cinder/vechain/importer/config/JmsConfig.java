package cloud.cinder.vechain.importer.config;

import cloud.cinder.vechain.importer.block.continuous.BlockAddedListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JmsConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    @ConditionalOnProperty(name = "cloud.cinder.vechain.queue-block-added-transaction-import", havingValue = "true")
    SimpleMessageListenerContainer blockAddedContainer(final ConnectionFactory connectionFactory,
                                                       MessageListenerAdapter listenerAdapter,
                                                       @Value("${cloud.cinder.queue.vechain-block-added}") final String queueName) {
        return createContainer(connectionFactory, listenerAdapter, queueName);
    }

    @Bean
    @ConditionalOnProperty(name = "cloud.cinder.vechain.queue-block-added-transaction-import", havingValue = "true")
    MessageListenerAdapter blockAddedListenerAdapter(final BlockAddedListener receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    Queue approvedClaimQueue(@Value("${cloud.cinder.queue.vechain-block-added}") final String queueName) {
        return declareQueue(queueName);
    }


    private SimpleMessageListenerContainer createContainer(final ConnectionFactory connectionFactory,
                                                           final MessageListenerAdapter listenerAdapter,
                                                           final String queueName) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        container.setAutoDeclare(true);
        container.setConcurrentConsumers(5);
        return container;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory);
    }

    private Queue declareQueue(@Value("${io.fundrequest.azrael.queue.claim}") String queueName) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "");
        arguments.put("x-dead-letter-routing-key", queueName + ".dlq");
        return new Queue(queueName, true, false, false, arguments);
    }
}
