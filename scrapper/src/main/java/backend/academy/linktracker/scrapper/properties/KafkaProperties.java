package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.kafka")
@Validated
public record KafkaProperties(
        @NotBlank String topicName,
        @NotBlank String dlqTopicName,
        @Positive int replicas,
        @Positive int partitions) {}
