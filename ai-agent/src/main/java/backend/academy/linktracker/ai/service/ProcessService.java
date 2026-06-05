package backend.academy.linktracker.ai.service;

import backend.academy.linktracker.ai.broker.BrokerSender;
import backend.academy.linktracker.ai.mapper.NotificationMapper;
import backend.academy.linktracker.ai.util.MessageFilter;
import backend.academy.linktracker.ai.dto.NotificationDto;
import backend.academy.linktracker.avro.ProcessedLinkUpdate;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.UUID;

@Service
@Validated
@AllArgsConstructor
public class ProcessService {
    private final MessageFilter filter;
    private final AIService aiService;
    private final MessageFormater formater;
    private final BrokerSender sender;
    private final NotificationMapper mapper;

    //TODO убрать проверку на идемпотентность и реализовать ее только на стороне bot
    public void process(@Valid NotificationDto notification) {
        if (!filter.filter(notification)) {
            return;
        }

        String text = aiService.summarizing(notification.description());
        String formattedText = formater.formate(text, notification.url(), notification.author());

        ProcessedLinkUpdate update =
                mapper.toProcessedLinkUpdate(notification, UUID.randomUUID(), formattedText);
        sender.sendNotification(update);
    }
}
