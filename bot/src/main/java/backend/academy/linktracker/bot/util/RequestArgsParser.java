package backend.academy.linktracker.bot.util;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class RequestArgsParser {

    public List<String> parseTags(String tags) {
        return Arrays.stream(tags.split(","))
            .map(String::trim)
            .filter(tag -> !tag.isEmpty())
            .toList();
    }

    public String parseRemoveLink(String message) {
        return parseCommandArgs(message).getFirst();
    }

    private List<String> parseCommandArgs(String message) {
        return Arrays.stream(message.split(" "))
            .skip(1)
            .map(String::trim)
            .filter(tag -> !tag.isEmpty())
            .toList();
    }
}
