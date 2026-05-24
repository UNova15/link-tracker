package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.avro.LinkUpdateEvent;
import jakarta.validation.ConstraintViolationException;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.kafka.max-retries}")
    long numberOfAttempts;

    @Value("${app.kafka.time-out}")
    long timeOut;

    @Bean
    DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<String, LinkUpdateEvent> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(
                kafkaTemplate, (record, exception) -> new TopicPartition(record.topic() + "-dlq", record.partition()));
    }

    @Bean
    DefaultErrorHandler defaultErrorHandler(DeadLetterPublishingRecoverer recoverer) {
        var defaultErrorHandler = new DefaultErrorHandler(recoverer, new FixedBackOff(timeOut, numberOfAttempts));
        defaultErrorHandler.addNotRetryableExceptions(
                MethodArgumentNotValidException.class,
                ConstraintViolationException.class,
                DeserializationException.class);
        return defaultErrorHandler;
    }
}
