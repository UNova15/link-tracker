package backend.academy.linktracker.bot.util;

import backend.academy.linktracker.bot.dto.LinkResponse;
import backend.academy.linktracker.bot.dto.ListLinkResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ListCommandHelper {

    public List<String> filterLinksByTag(ListLinkResponse response, Optional<String> tag) {
        return tag.map(s -> response.links().stream()
                        .filter(link -> link.tags().contains(s))
                        .map(LinkResponse::url)
                        .toList())
                .orElseGet(
                        () -> response.links().stream().map(LinkResponse::url).toList());
    }

    public String formateResponse(List<String> links) {
        StringBuilder builder = new StringBuilder();
        builder.append("Отслеживаемые ссылки:\n");
        for (String link : links) {
            builder.append(link);
            builder.append("\n");
        }
        return builder.toString();
    }
}
