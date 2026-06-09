package backend.academy.linktracker.ai.service;

import backend.academy.linktracker.ai.domain.Notification;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageFilter {
    private final long minLength;
    private final Set<String> excludedAuthors;
    private final Pattern pattern;

    public boolean filter(Notification notification) {
        if (notification.description().length() <= minLength) {
            return false;
        }

        String author = notification.author().toLowerCase();
        if (excludedAuthors.contains(author)) {
            return false;
        }

        return !pattern.matcher(notification.description()).find();
    }
}
