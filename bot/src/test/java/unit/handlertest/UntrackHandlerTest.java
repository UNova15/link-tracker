package unit.handlertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.linktracker.bot.scrapperclient.ScrapperLinkClient;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.dto.ApiErrorResponse;
import backend.academy.linktracker.bot.dto.RemoveLinkRequest;
import backend.academy.linktracker.bot.exception.ScrapperClientException;
import backend.academy.linktracker.bot.handler.command.UntrackHandler;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.state.NewState;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UntrackHandlerTest {
    @Mock
    private AwaitCommandState awaitCommandState;

    @Mock
    private ScrapperLinkClient client;

    @Mock
    private RequestArgsParser parser;

    @InjectMocks
    private UntrackHandler untrackHandler;

    @Test
    void handle_withValidLinkAndNullSessionData_returnValidMessageAndChangeStateToAwaitCommandState() {
        String command = "/untrack ";
        String url = "https://1234";
        String userMessage = command + " " + url;
        String expectedMessage = "Ссылка успешно удалена";
        long chatId = 1;
        UserMessage message = new UserMessage(chatId, userMessage);
        SessionData sessionData = new SessionData(null);

        when(parser.parseFirstCommandArgument(userMessage)).thenReturn(Optional.of(url));

        String actualMessage = untrackHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(awaitCommandState, sessionData.getState());
        assertNull(sessionData.getLink());
        assertNull(sessionData.getTags());

        verify(parser).parseFirstCommandArgument(userMessage);

        ArgumentCaptor<RemoveLinkRequest> captured = ArgumentCaptor.forClass(RemoveLinkRequest.class);
        verify(client).removeLink(eq(chatId), captured.capture());

        RemoveLinkRequest actualArgs = captured.getValue();
        assertEquals(url, actualArgs.link());
    }

    @Test
    void handle_withEmptyLinkAndNullSessionData_returnMissingLinkMessageAndChangeStateToAwaitCommandState() {
        String command = "/untrack ";
        String url = "";
        String userMessage = command + " " + url;
        String expectedMessage = "Отсутствует ссылка на удаляемый ресурс";
        long chatId = 1;
        UserMessage message = new UserMessage(chatId, userMessage);
        SessionData sessionData = new SessionData(null);

        when(parser.parseFirstCommandArgument(userMessage)).thenReturn(Optional.empty());

        String actualMessage = untrackHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(awaitCommandState, sessionData.getState());
        assertNull(sessionData.getLink());
        assertNull(sessionData.getTags());

        verify(parser).parseFirstCommandArgument(userMessage);

        verify(client, never()).removeLink(eq(chatId), any(RemoveLinkRequest.class));
    }

    @Test
    void handle_withValidLinkAndNewStateSessionData_returnValidMessageAndChangeStateToAwaitCommandState() {
        String command = "/untrack ";
        String url = "https://1234";
        String userMessage = command + " " + url;
        String expectedMessage = "Ссылка успешно удалена";
        long chatId = 1;
        NewState newState = mock(NewState.class);
        UserMessage message = new UserMessage(chatId, userMessage);
        SessionData sessionData = new SessionData(newState);

        when(parser.parseFirstCommandArgument(userMessage)).thenReturn(Optional.of(url));

        String actualMessage = untrackHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(awaitCommandState, sessionData.getState());
        assertNull(sessionData.getLink());
        assertNull(sessionData.getTags());

        verify(parser).parseFirstCommandArgument(userMessage);

        ArgumentCaptor<RemoveLinkRequest> captured = ArgumentCaptor.forClass(RemoveLinkRequest.class);
        verify(client).removeLink(eq(chatId), captured.capture());

        RemoveLinkRequest actualArgs = captured.getValue();
        assertEquals(url, actualArgs.link());
    }

    @Test
    void handle_withScrapperException_returnErrorMessage() {
        String command = "/untrack ";
        String url = "https://1234";
        String userMessage = command + " " + url;
        String expectedMessage = "Ошибка удаления ссылки";
        long chatId = 1;
        UserMessage message = new UserMessage(chatId, userMessage);
        SessionData sessionData = new SessionData(null);

        when(parser.parseFirstCommandArgument(userMessage)).thenReturn(Optional.of(url));
        ScrapperClientException exception =
                new ScrapperClientException(new ApiErrorResponse("", "", "", "", new String[] {""}));

        doThrow(exception).when(client).removeLink(anyLong(), any(RemoveLinkRequest.class));

        String actualMessage = untrackHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertNull(sessionData.getState());
    }
}
