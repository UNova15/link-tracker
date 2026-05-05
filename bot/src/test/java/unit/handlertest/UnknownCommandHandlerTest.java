package unit.handlertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.UnknownCommandHandler;
import org.junit.jupiter.api.Test;

public class UnknownCommandHandlerTest {

    private final UnknownCommandHandler unknownCommandHandler = new UnknownCommandHandler();

    @Test
    void handle_withValidMessageAndSessionData_returnValidMessageAndDoesNotChangeSessionData() {
        String expectedMessage = "Неизвестная команда. Воспользуйтесь /help, чтобы посмотреть список доступных команд.";
        UserMessage message = new UserMessage(1, "Text");
        SessionData sessionData = new SessionData(null);

        String actualMessage = unknownCommandHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertNull(sessionData.getTags());
        assertNull(sessionData.getLink());
        assertNull(sessionData.getState());
    }
}
