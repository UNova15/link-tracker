package unit.statetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.UnknownUserHandler;
import backend.academy.linktracker.bot.handler.command.StartHandler;
import backend.academy.linktracker.bot.state.NewState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NewStateTest {

    @Mock
    private StartHandler startHandler;

    @Mock
    private UnknownUserHandler unknownUserHandler;

    @InjectMocks
    private NewState newState;

    @Test
    void process_withStartCommand_invokeStartHandler() {
        long userId = 1;
        String command = "/start";
        String userMessage = "/start";
        String expectedMessage = "Добро пожаловать! Используйте /help, чтобы посмотреть доступные команды.";
        UserMessage message = new UserMessage(userId, userMessage);
        SessionData sessionData = new SessionData(newState);

        when(startHandler.handle(message, sessionData)).thenReturn(expectedMessage);
        when(startHandler.getName()).thenReturn(command);

        String actualMessage = newState.process(message, sessionData);

        verify(startHandler).handle(message, sessionData);
        verify(unknownUserHandler, never()).handle(any(UserMessage.class), any(SessionData.class));
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void process_withInvalidCommand_invokeUnknownUserHandler() {
        long userId = 1;
        String userMessage = "https://1234";
        String command = "/start";
        String expectedMessage = "Неизвестная команда. Чтобы начать диалог выполните команду /start";
        UserMessage message = new UserMessage(userId, userMessage);
        SessionData sessionData = new SessionData(newState);

        when(unknownUserHandler.handle(message, sessionData)).thenReturn(expectedMessage);
        when(startHandler.getName()).thenReturn(command);

        String actualMessage = newState.process(message, sessionData);

        verify(unknownUserHandler).handle(message, sessionData);
        verify(startHandler, never()).handle(any(UserMessage.class), any(SessionData.class));
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void process_withNonStartCommand_invokeStartHandler() {
        long userId = 1;
        String command = "/start";
        String userMessage = "/list";
        String expectedMessage = "Неизвестная команда. Чтобы начать диалог выполните команду /start";
        UserMessage message = new UserMessage(userId, userMessage);
        SessionData sessionData = new SessionData(newState);

        when(unknownUserHandler.handle(message, sessionData)).thenReturn(expectedMessage);
        when(startHandler.getName()).thenReturn(command);

        String actualMessage = newState.process(message, sessionData);

        verify(unknownUserHandler).handle(message, sessionData);
        verify(startHandler, never()).handle(any(UserMessage.class), any(SessionData.class));
        assertEquals(expectedMessage, actualMessage);
    }
}
