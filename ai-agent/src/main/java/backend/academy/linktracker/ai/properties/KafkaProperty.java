package backend.academy.linktracker.ai.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record KafkaProperty(String rawUpdatesTopic, String processedUpdatesTopic, int replicas, int partitions) {}
