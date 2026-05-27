package backend.academy.linktracker.bot.unit.handlertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.handler.command.CancelHandler;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.state.NewState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CancelHandlerTest {
    @Mock
    private AwaitCommandState awaitCommandState;

    @InjectMocks
    private CancelHandler cancelHandler;

    @Test
    void handle_withNullState_changeStateToAwaitCommandStateAndCancelMessage() {
        String expectedMessage = "Операция отменена";
        UserMessage message = new UserMessage(1, "Text");
        SessionData sessionData = new SessionData(null);

        String actualMessage = cancelHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(awaitCommandState, sessionData.getState());
    }

    @Test
    void handle_withNewState_changeStateToAwaitCommandStateAndCancelMessage() {
        String expectedMessage = "Операция отменена";
        UserMessage message = new UserMessage(1, "Text");
        NewState newState = mock(NewState.class);
        SessionData sessionData = new SessionData(newState);

        String actualMessage = cancelHandler.handle(message, sessionData);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(awaitCommandState, sessionData.getState());
    }
}
