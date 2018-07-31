package cloud.cinder.web.config;

import cloud.cinder.web.block.continuous.BlockAddedListener;
import cloud.cinder.web.token.listener.UserTokenImportListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfig {

    @Bean
    @ConditionalOnProperty(name = "cloud.cinder.ethereum.queue-block-added-transaction-import", havingValue = "true")
    SimpleMessageListenerContainer blockAddedContainer(final ConnectionFactory connectionFactory,
                                                       MessageListenerAdapter listenerAdapter,
                                                       @Value("${cloud.cinder.queue.block-added}") final String queueName) {
        return createContainer(connectionFactory, listenerAdapter, queueName);
    }

    @Bean
    @ConditionalOnProperty(name = "cloud.cinder.ethereum.queue-block-added-transaction-import", havingValue = "true")
    MessageListenerAdapter blockAddedListenerAdapter(final BlockAddedListener receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


    @Bean
    @ConditionalOnProperty(name = "token-transfer-queue-import", havingValue = "true")
    SimpleMessageListenerContainer container(final ConnectionFactory connectionFactory,
                                             final MessageListenerAdapter listenerAdapter,
                                             @Value("${cloud.cinder.queue.user-token-import}") final String queueName) {
        return createContainer(connectionFactory, listenerAdapter, queueName);
    }

    @Bean
    @ConditionalOnProperty(name = "token-transfer-queue-import", havingValue = "true")
    MessageListenerAdapter listenerAdapter(final UserTokenImportListener receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    private SimpleMessageListenerContainer createContainer(final ConnectionFactory connectionFactory,
                                                           final MessageListenerAdapter listenerAdapter,
                                                           final String queueName) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        container.setConcurrentConsumers(5);
        return container;
    }
}
