package backend.academy.linktracker.ai.service;

import backend.academy.linktracker.ai.broker.BrokerSender;
import backend.academy.linktracker.ai.dto.AggregatedNotification;
import backend.academy.linktracker.ai.dto.NotificationDto;
import backend.academy.linktracker.ai.mapper.NotificationMapper;
import backend.academy.linktracker.ai.util.MessageFormater;
import backend.academy.linktracker.avro.ProcessedLinkUpdate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ProcessService {
    private final AIService aiService;
    private final MessageFormater formater;
    private final BrokerSender sender;
    private final NotificationMapper mapper;

    public void process(AggregatedNotification aggregatedNotification) {
        List<NotificationDto> notificationDtos = aggregatedNotification.notificationDtos().stream()
                .map(notification -> {
                    String summarizing = aiService.summarizing(notification.description());
                    return notification.withNewDescription(summarizing);
                })
                .toList();

        String message = formater.formate(notificationDtos);
        UUID idempotencyKey = UUID.randomUUID();
        ProcessedLinkUpdate update = mapper.toProcessedLinkUpdate(notificationDtos, idempotencyKey, message);

        sender.sendNotification(update);
    }
}
