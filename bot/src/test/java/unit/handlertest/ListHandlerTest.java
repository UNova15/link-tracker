package unit.handlertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.linktracker.bot.scrapperclient.ScrapperLinkClient;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.dto.ApiErrorResponse;
import backend.academy.linktracker.bot.dto.ListLinkResponse;
import backend.academy.linktracker.bot.exception.ScrapperClientException;
import backend.academy.linktracker.bot.handler.command.ListHandler;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.state.NewState;
import backend.academy.linktracker.bot.util.ListCommandHelper;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ListHandlerTest {
    @Mock
    private ScrapperLinkClient scrapperLinkClient;

    @Mock
    private ListCommandHelper listCommandHelper;

    @Mock
    private RequestArgsParser parser;

    @Mock
    private AwaitCommandState state;

    @InjectMocks
    private ListHandler listHandler;

    @Test
    void handle_withNewState_returnListOfLinks() {
        long userId = 1;
        String link = "https://1234";
        String expectedMessage = "Отслеживаемые ссылки:\n" + link;
        UserMessage message = new UserMessage(userId, "text");
        NewState newState = mock(NewState.class);
        ListLinkResponse response = mock(ListLinkResponse.class);
        SessionData sessionData = new SessionData(newState);

        when(parser.parseFirstCommandArgument(message.text())).thenReturn(Optional.empty());
        when(scrapperLinkClient.getLinks(message.id())).thenReturn(response);
        when(listCommandHelper.filterLinksByTag(response, Optional.empty())).thenReturn(List.of(link));
        when(listCommandHelper.formateResponse(List.of(link))).thenReturn(expectedMessage);

        String actualMessage = listHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        verify(parser).parseFirstCommandArgument(message.text());
        verify(scrapperLinkClient).getLinks(message.id());
        verify(listCommandHelper).filterLinksByTag(response, Optional.empty());
        verify(listCommandHelper).formateResponse(List.of(link));

        assertEquals(state, sessionData.getState());
        assertNull(sessionData.getLink());
        assertNull(sessionData.getTags());
    }

    @Test
    void handle_withScrapperApiException_returnListOfLinks() {
        long userId = 1;
        String link = "https://1234";
        String expectedMessage = "Ошибка при поиске ссылок. Повторите попытке позже";
        UserMessage message = new UserMessage(userId, "text");
        NewState newState = mock(NewState.class);
        ListLinkResponse response = mock(ListLinkResponse.class);
        SessionData sessionData = new SessionData(newState);

        when(parser.parseFirstCommandArgument(message.text())).thenReturn(Optional.empty());
        doThrow(new ScrapperClientException(mock(ApiErrorResponse.class)))
                .when(scrapperLinkClient)
                .getLinks(message.id());

        String actualMessage = listHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        verify(parser).parseFirstCommandArgument(message.text());
        verify(scrapperLinkClient).getLinks(message.id());
        verify(listCommandHelper, never()).filterLinksByTag(response, Optional.empty());
        verify(listCommandHelper, never()).formateResponse(List.of(link));

        assertEquals(state, sessionData.getState());
        assertNull(sessionData.getLink());
        assertNull(sessionData.getTags());
    }

    @Test
    void handle_withTag_returnListOfLinks() {
        long userId = 1;
        String link = "https://1234";
        String tag = "work";
        String expectedMessage = "Отслеживаемые ссылки:\n" + link;
        UserMessage message = new UserMessage(userId, "text");
        NewState newState = mock(NewState.class);
        ListLinkResponse response = mock(ListLinkResponse.class);
        SessionData sessionData = new SessionData(newState);

        when(parser.parseFirstCommandArgument(message.text())).thenReturn(Optional.of(tag));
        when(scrapperLinkClient.getLinks(message.id())).thenReturn(response);
        when(listCommandHelper.filterLinksByTag(response, Optional.of(tag))).thenReturn(List.of(link));
        when(listCommandHelper.formateResponse(List.of(link))).thenReturn(expectedMessage);

        String actualMessage = listHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        verify(parser).parseFirstCommandArgument(message.text());
        verify(scrapperLinkClient).getLinks(message.id());
        verify(listCommandHelper).filterLinksByTag(response, Optional.of(tag));
        verify(listCommandHelper).formateResponse(List.of(link));

        assertEquals(state, sessionData.getState());
        assertNull(sessionData.getLink());
        assertNull(sessionData.getTags());
    }
}
