package backend.academy.linktracker.ai.service;

import backend.academy.linktracker.ai.domain.Notification;
import backend.academy.linktracker.ai.domain.Priority;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PrioritizeService {
    private final Pattern highKeyWords;
    private final Pattern lowKeyWords;

    public Priority prioritize(Notification notificationDto) {
        if (highKeyWords.matcher(notificationDto.description()).find()) {
            return Priority.HIGH;
        }

        if (lowKeyWords.matcher(notificationDto.description()).find()) {
            return Priority.LOW;
        }
        return Priority.MEDIUM;
    }
}
