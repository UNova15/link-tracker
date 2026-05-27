package backend.academy.linktracker.bot.unit.handlertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.UnknownUserHandler;
import org.junit.jupiter.api.Test;

public class UnknownUserHandlerTest {

    private final UnknownUserHandler unknownUserHandler = new UnknownUserHandler();

    @Test
    void handle_withMessageAndSessionData_returnValidMessageAndDoesNotChangeSessionData() {
        String expectedMessage = "Неизвестная команда. Чтобы начать диалог выполните команду /start";
        UserMessage message = new UserMessage(1, "Text");
        SessionData sessionData = new SessionData(null);

        String actualMessage = unknownUserHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertNull(sessionData.getState());
        assertNull(sessionData.getLink());
        assertNull(sessionData.getTags());
    }
}
