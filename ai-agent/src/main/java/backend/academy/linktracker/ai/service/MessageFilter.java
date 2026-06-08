package backend.academy.linktracker.ai.service;

import backend.academy.linktracker.ai.dto.NotificationDto;
import backend.academy.linktracker.ai.properties.FilterProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageFilter {
    private final FilterProperties properties;

    public boolean filter(NotificationDto notificationDto) {
        if (notificationDto.description().length() <= properties.minLength()) {
            return false;
        }

        String author = notificationDto.author().toLowerCase();
        if (properties.excludedAuthors().contains(author)) {
            return false;
        }

        String description = notificationDto.description().toLowerCase();
        return !findStopWords(description);
    }

    private boolean findStopWords(String description) {

        for (String stopWord : properties.stopWords()) {
            if (description.contains(stopWord)) {
                return true;
            }
        }
        return false;
    }
}
