package backend.academy.linktracker.bot.unit.handlertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.command.HelpHandler;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.state.NewState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HelpHandlerTest {
    @Mock
    private CommandRegistry registry;

    @Mock
    private AwaitCommandState awaitCommandState;

    @InjectMocks
    private HelpHandler helpHandler;

    @Test
    void handle_withNullState_returnListOfCommandsAndChangeStateToAwaitCommandStateAwaitCommandState() {
        String commands = "/track /help";
        String expectedMessage = "Список доступных команд:\n" + commands;
        UserMessage message = new UserMessage(1, "Text");
        SessionData sessionData = new SessionData(null);

        when(registry.toString()).thenReturn(commands);

        String actualMessage = helpHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(sessionData.getState(), awaitCommandState);
    }

    @Test
    void handle_withNewState_returnListOfCommandsAndChangeStateToAwaitCommandStateAwaitCommandState() {
        String commands = "/track /help";
        String expectedMessage = "Список доступных команд:\n" + commands;
        UserMessage message = new UserMessage(1, "Text");
        NewState newState = mock(NewState.class);
        SessionData sessionData = new SessionData(newState);

        when(registry.toString()).thenReturn(commands);

        String actualMessage = helpHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(sessionData.getState(), awaitCommandState);
    }
}
