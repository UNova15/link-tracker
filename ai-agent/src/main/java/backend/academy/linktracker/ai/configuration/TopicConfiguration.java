package backend.academy.linktracker.ai.configuration;

import backend.academy.linktracker.ai.properties.KafkaProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfiguration {

    @Bean
    public NewTopic processedMessagesTopic(KafkaProperties properties) {
        return TopicBuilder.name(properties.processedUpdatesTopic())
                .partitions(properties.partitions())
                .replicas(properties.replicas())
                .build();
    }

    @Bean
    public NewTopic aggregatedUpdatesTopic(KafkaProperties properties) {
        return TopicBuilder.name(properties.aggregatedUpdatesTopic())
                .partitions(properties.partitions())
                .replicas(properties.replicas())
                .build();
    }
}
