package backend.academy.linktracker.ai.util;

import backend.academy.linktracker.ai.domain.Notification;
import backend.academy.linktracker.ai.dto.NotificationDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageFormater {
    private static final String SINGLE_MESSAGE_TEMPLATE = """
        *Новое изменение по ссылке: %s*
        Автор: %s
        Приоритет: %s
        Описание: %s
        """.replace("\n", "%n");

    private static final String GENERIC_TEMPLATE = """
        * %d Новое изменение по ссылке: %s *
        Автор: %s
        Приоритет: %s
        Описание: %s
        """.replace("\n", "%n");

    public String formate(List<Notification> notifications) {
        if (notifications.size() == 1) {
            Notification notification = notifications.getFirst();
            return SINGLE_MESSAGE_TEMPLATE.formatted(
                    notification.url(),
                    notification.author(),
                    notification.priority().toString(),
                    notification.description());
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < notifications.size(); i++) {
            int number = i + 1;
            Notification notification = notifications.get(i);
            String formattedText = GENERIC_TEMPLATE.formatted(
                    number,
                    notification.url(),
                    notification.author(),
                    notification.priority().toString(),
                    notification.description());

            builder.append(formattedText);
            builder.append("\n");
        }
        return builder.toString();
    }
}
