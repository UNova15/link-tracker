package backend.academy.linktracker.ai.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageFormater {
    private static final String TEMPLATE = """
        *Новое изменение по ссылке: %s*
        Автор: %s
        Описание: %s
        """.replace("\n", "%n");

    public String formate(String description, String url, String author) {
        return TEMPLATE.formatted(url, author, description);
    }
}
