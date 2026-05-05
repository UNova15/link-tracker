package backend.academy.linktracker.bot.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class RequestArgsParser {

    public List<String> parseTags(String tags) {
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .toList();
    }

    public String parseCommand(String command) {
        if (command == null || command.isBlank()) {
            return "";
        }
        return command.trim().split(" ")[0];
    }

    public Optional<String> parseFirstCommandArgument(String message) {
        return parseCommandArgs(message).stream().findFirst();
    }

    private List<String> parseCommandArgs(String message) {
        return Arrays.stream(message.split(" "))
                // пропуск команды
                .skip(1)
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .toList();
    }
}
