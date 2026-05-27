package backend.academy.linktracker.bot.unit.statetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.handler.statehandler.AwaitLinkHandler;
import backend.academy.linktracker.bot.state.AwaitLinkState;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AwaitLinkStateTest {
    @Mock
    private CommandRegistry registry;

    @Mock
    private AwaitLinkHandler awaitLinkHandler;

    @InjectMocks
    private AwaitLinkState state;

    @Test
    void process_withLink_invokeAwaitLink() {
        long userId = 1;
        String userMessage = "https://12345";
        String expectedMessage = "Введите через запятую теги для ссылки";
        UserMessage message = new UserMessage(userId, userMessage);
        SessionData sessionData = new SessionData(state);

        when(registry.getCommandHandler(userMessage)).thenReturn(Optional.empty());
        when(awaitLinkHandler.handle(message, sessionData)).thenReturn(expectedMessage);

        String actualMessage = state.process(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        verify(registry).getCommandHandler(userMessage);
        verify(awaitLinkHandler).handle(message, sessionData);
    }

    @Test
    void process_withCommand_invokeAwaitLink() {
        long userId = 1;
        String userMessage = "/help";
        String expectedMessage = "Список доступных команд:\n";
        UserMessage message = new UserMessage(userId, userMessage);
        SessionData sessionData = new SessionData(state);

        Handler handler = mock(Handler.class);

        when(registry.getCommandHandler(userMessage)).thenReturn(Optional.of(handler));
        when(handler.handle(message, sessionData)).thenReturn(expectedMessage);

        String actualMessage = state.process(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        verify(registry).getCommandHandler(userMessage);
        verify(handler).handle(message, sessionData);
        verify(awaitLinkHandler, never()).handle(any(UserMessage.class), any(SessionData.class));
    }
}
