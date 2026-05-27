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
import backend.academy.linktracker.bot.handler.statehandler.AddLinkHandler;
import backend.academy.linktracker.bot.state.AwaitTagState;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AwaitTagStateTest {

    @Mock
    private AddLinkHandler addLinkHandler;

    @Mock
    private CommandRegistry commandRegistry;

    @InjectMocks
    private AwaitTagState awaitTagState;

    @Test
    void process_withTag_invokeAddLinkHandler() {
        long userId = 1;
        String userMessage = "Tag1 Tag2";
        String expectedMessage = "Ссылка успешно сохранена";
        UserMessage message = new UserMessage(userId, userMessage);
        SessionData sessionData = new SessionData(awaitTagState);

        when(commandRegistry.getCommandHandler(userMessage)).thenReturn(Optional.empty());
        when(addLinkHandler.handle(message, sessionData)).thenReturn(expectedMessage);

        String actualMessage = awaitTagState.process(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        verify(commandRegistry).getCommandHandler(userMessage);
        verify(addLinkHandler).handle(message, sessionData);
    }

    @Test
    void process_withCommand_invokeAddLinkHandler() {
        long userId = 1;
        String userMessage = "/help";
        String expectedMessage = "Список доступных команд:\n";
        UserMessage message = new UserMessage(userId, userMessage);
        SessionData sessionData = new SessionData(awaitTagState);

        Handler handler = mock(Handler.class);

        when(commandRegistry.getCommandHandler(userMessage)).thenReturn(Optional.of(handler));
        when(handler.handle(message, sessionData)).thenReturn(expectedMessage);

        String actualMessage = awaitTagState.process(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        verify(commandRegistry).getCommandHandler(userMessage);
        verify(handler).handle(message, sessionData);
        verify(addLinkHandler, never()).handle(any(UserMessage.class), any(SessionData.class));
    }
}
