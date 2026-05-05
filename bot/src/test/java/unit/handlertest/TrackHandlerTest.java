package unit.handlertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.command.TrackHandler;
import backend.academy.linktracker.bot.state.AwaitLinkState;
import backend.academy.linktracker.bot.state.NewState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrackHandlerTest {

    @Mock
    private AwaitLinkState awaitLink;

    @InjectMocks
    private TrackHandler trackHandler;

    @Test
    void handle_withNullState_returnMessageAndChangeStateToAwaitLinkState() {
        String expectedMessage = "Введите ссылку для отслеживания";
        UserMessage message = new UserMessage(1, "Text");
        SessionData sessionData = new SessionData(null);

        String actualMessage = trackHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(awaitLink, sessionData.getState());
    }

    @Test
    void handle_withNewState_returnMessageAndChangeStateToAwaitLinkState() {
        String expectedMessage = "Введите ссылку для отслеживания";
        UserMessage message = new UserMessage(1, "Text");
        NewState newState = mock(NewState.class);
        SessionData sessionData = new SessionData(newState);

        String actualMessage = trackHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(awaitLink, sessionData.getState());
    }
}
