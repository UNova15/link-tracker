package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.avro.LinkUpdateEvent;
import backend.academy.linktracker.bot.properties.KafkaProperties;
import jakarta.validation.ConstraintViolationException;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfiguration {

    @Bean
    DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<String, LinkUpdateEvent> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(
                kafkaTemplate, (record, exception) -> new TopicPartition(record.topic() + "-dlq", record.partition()));
    }

    @Bean
    DefaultErrorHandler defaultErrorHandler(KafkaProperties properties, DeadLetterPublishingRecoverer recoverer) {
        var defaultErrorHandler =
                new DefaultErrorHandler(recoverer, new FixedBackOff(properties.getTimeout(), properties.getRetries()));
        defaultErrorHandler.addNotRetryableExceptions(
                MethodArgumentNotValidException.class,
                ConstraintViolationException.class,
                DeserializationException.class);
        return defaultErrorHandler;
    }

}
