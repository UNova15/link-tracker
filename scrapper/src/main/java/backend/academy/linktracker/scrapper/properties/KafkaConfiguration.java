package backend.academy.linktracker.scrapper.properties;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "sender", havingValue = "mq", matchIfMissing = true)
public class KafkaConfiguration {

    @Value("${app.kafka.topic-name}")
    private String topicName;

    @Value("${app.kafka.replicas}")
    private int replicas;

    @Value("${app.kafka.partitions}")
    private int partitions;

    @Bean
    public NewTopic updates(){
        return TopicBuilder.name(topicName)
            .partitions(replicas)
            .replicas(partitions)
            .build();
    }
}
