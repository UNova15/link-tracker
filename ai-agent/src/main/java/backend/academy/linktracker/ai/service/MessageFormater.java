package backend.academy.linktracker.ai.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageFormater {
    private static final String TEMPLATE = """
        *Новое изменение по ссылке: %s*
        Автор: %s
        Описание: %s
        """;

    public String formate(String description, String url, String author) {
        return TEMPLATE.formatted(url,author,description);
    }
}
