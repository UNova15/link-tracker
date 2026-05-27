package backend.academy.linktracker.bot.unit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import backend.academy.linktracker.bot.util.RequestArgsParser;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RequestParserTest {
    private final RequestArgsParser parser = new RequestArgsParser();

    @Test
    void parseTags_withValidTags_returnListOfTags() {
        String tags = "Tag1, Tag2 , Tag3,Tag4, ";
        List<String> expectedTags = List.of("Tag1", "Tag2", "Tag3", "Tag4");

        List<String> actualTags = parser.parseTags(tags);

        assertEquals(expectedTags, actualTags);
    }

    @Test
    void parseTags_withEmptyString_returnListOfTags() {
        RequestArgsParser parser = new RequestArgsParser();
        String tags = " ";
        List<String> expectedTags = List.of();

        List<String> actualTags = parser.parseTags(tags);

        assertEquals(expectedTags, actualTags);
    }

    @Test
    void parseCommand_withCommandWithArgs_returnCommand() {
        String command = "/help 123";
        String expectedCommand = "/help";

        String actualCommand = parser.parseCommand(command);

        assertEquals(expectedCommand, actualCommand);
    }

    @Test
    void parseCommand_withCommand_returnCommand() {
        String command = "/help";
        String expectedCommand = "/help";

        String actualCommand = parser.parseCommand(command);

        assertEquals(expectedCommand, actualCommand);
    }

    @Test
    void parseCommand_withEmptyCommand_returnCommand() {
        String command = " ";
        String expectedCommand = "";

        String actualCommand = parser.parseCommand(command);

        assertEquals(expectedCommand, actualCommand);
    }

    @Test
    void parseLinkWithRemoveCommand_withCommandAndArgument_returnLink() {
        String commandWithLink = "/untrack https://123456";
        String expectedLink = "https://123456";

        String actualLink = parser.parseFirstCommandArgument(commandWithLink).orElse("");

        assertEquals(expectedLink, actualLink);
    }

    @Test
    void parseLinkWithRemoveCommand_withoutCommand_returnLink() {
        String commandWithLink = "https://123456";
        String expectedLink = "";

        String actualLink = parser.parseFirstCommandArgument(commandWithLink).orElse("");

        assertEquals(expectedLink, actualLink);
    }

    @Test
    void parseFirstCommandArgument_withoutArgument_returnLink() {
        String commandWithLink = "/untrack";
        String expectedLink = "";

        String actualLink = parser.parseFirstCommandArgument(commandWithLink).orElse("");

        assertEquals(expectedLink, actualLink);
    }
}
