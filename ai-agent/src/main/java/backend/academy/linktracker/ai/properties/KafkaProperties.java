package backend.academy.linktracker.ai.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.kafka")
@Validated
public record KafkaProperties(
        @NotBlank @URL String schemaRegistryUrl,
        @NotBlank String rawUpdatesTopic,
        @NotBlank String aggregatedUpdatesTopic,
        @NotBlank String processedUpdatesTopic,
        @Positive int replicas,
        @Positive int partitions,
        @PositiveOrZero int timeOut) {}
