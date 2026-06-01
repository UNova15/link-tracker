package backend.academy.linktracker.scrapper.configuration;

import backend.academy.linktracker.scrapper.properties.KafkaProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "sender", havingValue = "mq", matchIfMissing = true)
public class KafkaConfiguration {

    @Bean
    public NewTopic updates(KafkaProperties properties) {
        return TopicBuilder.name(properties.topicName())
                .partitions(properties.partitions())
                .replicas(properties.replicas())
                .build();
    }

    @Bean
    public NewTopic updatesDlq(KafkaProperties properties) {
        return TopicBuilder.name(properties.dlqTopicName())
                .partitions(properties.partitions())
                .replicas(properties.replicas())
                .build();
    }
}
