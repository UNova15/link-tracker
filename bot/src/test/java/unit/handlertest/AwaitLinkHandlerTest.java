package unit.handlertest;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.statehandler.AwaitLinkHandler;
import backend.academy.linktracker.bot.state.AwaitTagState;
import backend.academy.linktracker.bot.validator.LinkValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AwaitLinkHandlerTest {

    @Mock
    private AwaitTagState awaitTagState;
    @Mock
    private LinkValidator linkValidator;

    @InjectMocks
    private AwaitLinkHandler awaitLinkHandler;

    @Test
    void handle_withValidLinkAndNullState_changeDataSessionAndReturnMessage() {
        String expectedMessage = "Введите через запятую теги для ссылки";
        String url = "https://1234";
        UserMessage message = new UserMessage(1, url);
        SessionData sessionData = new SessionData(null);

        when(linkValidator.validate(url)).thenReturn(Optional.empty());

        String actualMessage = awaitLinkHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(sessionData.getState(), awaitTagState);
    }

    @Test
    void handle_withInvalidLinkAndNullState_changeDataSessionAndReturnMessage() {
        String expectedMessage = "Некорректный формат ссылки";
        String url = "htp:/1234";
        UserMessage message = new UserMessage(1, url);
        SessionData sessionData = new SessionData(null);

        when(linkValidator.validate(url)).thenReturn(Optional.of(expectedMessage));

        String actualMessage = awaitLinkHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertNull(sessionData.getState());
        assertNull(sessionData.getLink());
    }
}
