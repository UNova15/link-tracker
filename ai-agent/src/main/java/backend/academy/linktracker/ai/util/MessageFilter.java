package backend.academy.linktracker.ai.util;

import backend.academy.linktracker.ai.dto.NotificationDto;
import backend.academy.linktracker.ai.properties.FilterProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageFilter {
    private final FilterProperties properties;

    public boolean filter(NotificationDto notification) {
        if (notification.description().length() <= properties.minLength()) {
            return false;
        }

        String author = notification.author().toLowerCase();
        if (properties.excludedAuthors().contains(author)) {
            return false;
        }

        String description = notification.description().toLowerCase();
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
