package backend.academy.linktracker.ai.configuration;

import backend.academy.linktracker.ai.properties.KafkaProperty;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {

    @Bean
    public NewTopic processedMessagesTopic(KafkaProperty properties) {
        return TopicBuilder.name(properties.processedUpdatesTopic())
                .partitions(properties.partitions())
                .replicas(properties.replicas())
                .build();
    }
}
