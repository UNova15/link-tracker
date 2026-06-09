package backend.academy.linktracker.ai.broker;

import backend.academy.linktracker.ai.dto.AggregatedNotification;
import backend.academy.linktracker.ai.service.ProcessService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class BrokerListener {
    private final ProcessService service;

    @KafkaListener(topics = "${app.kafka.aggregated-updates-topic}", id = "ai-agent-group")
    public void listen(@Payload AggregatedNotification aggregatedNotification) {
        log.debug("Сообщение агрегированно и готово к последующей обработке {}", aggregatedNotification);
        service.process(aggregatedNotification);
    }
}
