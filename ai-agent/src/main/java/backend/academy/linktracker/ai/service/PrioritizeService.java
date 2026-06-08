package backend.academy.linktracker.ai.service;

import backend.academy.linktracker.ai.domain.Priority;
import backend.academy.linktracker.ai.dto.NotificationDto;
import backend.academy.linktracker.ai.properties.PrioritizationProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PrioritizeService {
    private final PrioritizationProperties properties;

    public Priority prioritize(NotificationDto notification) {
        for (String highKeyWord : properties.highKeywords()) {
            if (notification.description().contains(highKeyWord)) {
                return Priority.HIGH;
            }
        }

        for (String lowKeyWords : properties.lowKeywords()) {
            if (notification.description().contains(lowKeyWords)) {
                return Priority.LOW;
            }
        }

        return Priority.MEDIUM;
    }
}
