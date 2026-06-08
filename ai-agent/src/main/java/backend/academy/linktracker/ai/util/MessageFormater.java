package backend.academy.linktracker.ai.util;

import backend.academy.linktracker.ai.dto.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@AllArgsConstructor
public class MessageFormater {
    private static final String SINGLE_MESSAGE_TEMPLATE = """
        *Новое изменение по ссылке: %s*
        Автор: %s
        Описание: %s
        """.replace("\n", "%n");

    private static final String GENERIC_TEMPLATE = """
        * %d Новое изменение по ссылке: %s*
        Автор: %s
        Описание: %s
        """.replace("\n", "%n");

    public String formate(List<NotificationDto> notificationDtos) {
        if (notificationDtos.size() == 1) {
            NotificationDto notificationDto = notificationDtos.getFirst();
            return SINGLE_MESSAGE_TEMPLATE.formatted(
                    notificationDto.url(), notificationDto.author(), notificationDto.description());
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < notificationDtos.size(); i++) {
            int number = i + 1;
            NotificationDto notificationDto = notificationDtos.get(i);
            String formattedText = GENERIC_TEMPLATE.formatted(
                    number, notificationDto.url(), notificationDto.author(), notificationDto.description());

            builder.append(formattedText);
            builder.append("\n");
        }
        return builder.toString();
    }
}
