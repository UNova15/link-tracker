package backend.academy.linktracker.bot.unit.handlertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import backend.academy.linktracker.bot.client.ScrapperChatClient;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.dto.ApiErrorResponse;
import backend.academy.linktracker.bot.exception.ScrapperApiException;
import backend.academy.linktracker.bot.handler.command.StartHandler;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.state.NewState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;

@ExtendWith(MockitoExtension.class)
public class StartHandlerTest {

    @Mock
    private ScrapperChatClient client;

    @Mock
    private AwaitCommandState awaitCommand;

    @InjectMocks
    private StartHandler startHandler;

    @Test
    void handle_withNewState_returnSuccessMessage() {
        long userId = 1;
        String expectedMessage = "Добро пожаловать! Используйте /help, чтобы посмотреть доступные команды.";
        UserMessage message = new UserMessage(userId, "text");
        NewState newState = mock(NewState.class);
        SessionData sessionData = new SessionData(newState);

        String actualMessage = startHandler.handle(message, sessionData);

        verify(client).registrationChat(userId);

        assertEquals(awaitCommand, sessionData.getState());
        assertEquals(expectedMessage, actualMessage);
        assertNull(sessionData.getLink());
        assertNull(sessionData.getTags());
    }

    @Test
    void handle_withScrapperApiException_returnErrorMessage() {
        long userId = 1;
        String expectedMessage = "Ошибка сохранения пользователя. Попробуйте позже";
        UserMessage message = new UserMessage(userId, "text");
        SessionData sessionData = new SessionData(null);

        doThrow(new ScrapperApiException(mock(ApiErrorResponse.class), HttpStatusCode.valueOf(504)))
                .when(client)
                .registrationChat(userId);

        String actualMessage = startHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertNull(sessionData.getTags());
        assertNull(sessionData.getState());
        assertNull(sessionData.getLink());
    }
}
