package statetest;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.Handler;
import backend.academy.linktracker.bot.handler.UnknownCommandHandler;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AwaitCommandStateTest {

    @Mock
    private CommandRegistry registry;

    @Mock
    private RequestArgsParser parser;

    @InjectMocks
    private AwaitCommandState awaitCommandState;

    @Test
    void process_withValidCommand_invokeAppropriateHandler() {
        long userId = 1;
        String userMessage = "/help";
        UserMessage message = new UserMessage(userId, userMessage);
        SessionData sessionData = new SessionData(awaitCommandState);
        String expectedMessage = "Список доступных команд";

        Handler handler = mock(Handler.class);

        when(registry.getCommandHandler(userMessage)).thenReturn(Optional.of(handler));
        when(parser.parseCommand(userMessage)).thenReturn(userMessage);
        when(handler.handle(message, sessionData)).thenReturn(expectedMessage);

        String actualMessage = awaitCommandState.process(message, sessionData);

        assertEquals(expectedMessage,actualMessage);
        verify(handler).handle(message,sessionData);
        verify(parser).parseCommand(userMessage);
        verify(registry).getCommandHandler(userMessage);
        verify(registry,never()).getUnknownCommandHandler();
    }

    @Test
    void process_withInvalidCommand_invokeUnknownHandler() {
        long userId = 1;
        String userMessage = "https://1234";
        UserMessage message = new UserMessage(userId,userMessage);
        SessionData sessionData = new SessionData(awaitCommandState);
        String expectedMessage = "Неизвестная команда. Воспользуйтесь /help, чтобы посмотреть список доступных команд.";

        UnknownCommandHandler unknownCommandHandler = mock(UnknownCommandHandler.class);

        when(registry.getUnknownCommandHandler()).thenReturn(unknownCommandHandler);
        when(parser.parseCommand(userMessage)).thenReturn(userMessage);
        when(unknownCommandHandler.handle(message,sessionData)).thenReturn(expectedMessage);

        String actualMessage = awaitCommandState.process(message,sessionData);

        assertEquals(expectedMessage,actualMessage);
        verify(registry).getCommandHandler(userMessage);
        verify(registry).getUnknownCommandHandler();
        verify(parser).parseCommand(userMessage);
        verify(unknownCommandHandler).handle(message,sessionData);
    }
}
