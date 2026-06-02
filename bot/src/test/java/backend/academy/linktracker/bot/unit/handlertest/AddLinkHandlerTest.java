package backend.academy.linktracker.bot.unit.handlertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.dto.AddLinkRequest;
import backend.academy.linktracker.bot.dto.ApiErrorResponse;
import backend.academy.linktracker.bot.exception.ScrapperClientException;
import backend.academy.linktracker.bot.handler.statehandler.AddLinkHandler;
import backend.academy.linktracker.bot.client.ScrapperLinkClient;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.state.NewState;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AddLinkHandlerTest {

    @Mock
    private ScrapperLinkClient scrapperLinkClient;

    @Mock
    private RequestArgsParser parser;

    @Mock
    private AwaitCommandState awaitCommandState;

    @InjectMocks
    private AddLinkHandler addLinkHandler;

    @Test
    void handle_withValidTags_returnSuccessMessage() {
        long chatId = 1;
        String link = "https://12345";
        List<String> tags = List.of("Work", "Something");
        String messageText = "Work, Something";
        String expectedMessage = "Ссылка успешно сохранена";
        UserMessage message = new UserMessage(chatId, messageText);
        NewState newState = mock(NewState.class);
        SessionData session = new SessionData(newState);
        session.setLink(link);

        when(parser.parseTags(messageText)).thenReturn(tags);

        String actualMessage = addLinkHandler.handle(message, session);

        assertEquals(expectedMessage, actualMessage);
        verify(parser).parseTags(messageText);
        verify(scrapperLinkClient).addLink(eq(chatId), eq(new AddLinkRequest(link, tags)));

        assertEquals(awaitCommandState, session.getState());
        assertNull(session.getTags());
        assertNull(session.getLink());
    }

    @Test
    void handle_withEmptyTags_returnSuccessMessage() {
        long chatId = 1;
        String link = "https://12345";
        List<String> tags = List.of();
        String messageText = " ";
        String expectedMessage = "Ссылка успешно сохранена";
        UserMessage message = new UserMessage(chatId, messageText);
        NewState newState = mock(NewState.class);
        SessionData session = new SessionData(newState);
        session.setLink(link);

        when(parser.parseTags(messageText)).thenReturn(tags);

        String actualMessage = addLinkHandler.handle(message, session);

        assertEquals(expectedMessage, actualMessage);
        verify(parser).parseTags(messageText);
        verify(scrapperLinkClient).addLink(eq(chatId), eq(new AddLinkRequest(link, tags)));

        assertEquals(awaitCommandState, session.getState());
        assertNull(session.getTags());
        assertNull(session.getLink());
    }

    @Test
    void handle_withScrapperApiException_returnErrorMessage() {
        long chatId = 1;
        String link = "https://12345";
        List<String> tags = List.of("Work", "Something");
        String messageText = "Work, Something";
        String expectedMessage = "Ошибка сохранения ссылки";
        UserMessage message = new UserMessage(chatId, messageText);
        NewState newState = mock(NewState.class);
        SessionData session = new SessionData(newState);
        session.setLink(link);
        AddLinkRequest request = new AddLinkRequest(link, tags);

        when(parser.parseTags(messageText)).thenReturn(tags);
        doThrow(new ScrapperClientException(mock(ApiErrorResponse.class)))
                .when(scrapperLinkClient)
                .addLink(chatId, request);

        String actualMessage = addLinkHandler.handle(message, session);

        assertEquals(expectedMessage, actualMessage);
        verify(parser).parseTags(messageText);
        verify(scrapperLinkClient).addLink(eq(chatId), eq(new AddLinkRequest(link, tags)));

        assertEquals(awaitCommandState, session.getState());
        assertNull(session.getTags());
        assertNull(session.getLink());
    }
}
