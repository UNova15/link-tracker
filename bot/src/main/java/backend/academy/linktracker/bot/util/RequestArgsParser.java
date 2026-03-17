package backend.academy.linktracker.bot.util;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class RequestArgsParser {

    public List<String> parseTags(String tags) {
        return Arrays.stream(tags.split(","))
            .map(String::trim)
            .filter(tag -> !tag.isEmpty())
            .toList();
    }

    public String parseCommand(String command) {
        return command.split(" ")[0];
    }

    public Optional<String> parseRemoveLink(String message) {
        return parseCommandArgs(message).stream().findFirst();
    }

    public Optional<String> parseListTag(String message) {
        return parseCommandArgs(message).stream().findFirst();
    }

    private List<String> parseCommandArgs(String message) {
        return Arrays.stream(message.split(" "))
            //пропуск команды
            .skip(1)
            .map(String::trim)
            .filter(tag -> !tag.isEmpty())
            .toList();
    }
}
