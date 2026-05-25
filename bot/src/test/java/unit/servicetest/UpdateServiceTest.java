package unit.servicetest;

import backend.academy.linktracker.bot.service.UpdateService;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UpdateServiceTest {

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private UpdateService updateService;

    /*@Test
    void sendUpdateMessage_withValidLinkUpdate_invokeBotExecute() {
        List<Long> chatIds = List.of(101L, 202L, 303L);
        String expectedText = "New commit: http://1234.com";
        Notification notification = Notification.createNotification(1L, "http://1234.com", "New commit", chatIds);

        updateService.sendUpdateMessage(notification);

        verify(telegramBot, times(3)).execute(any(SendMessage.class));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        verify(telegramBot, times(3)).execute(argumentCaptor.capture());

        List<SendMessage> sendMessages = argumentCaptor.getAllValues();

        assertEquals(3, sendMessages.size());
        assertEquals(101L, sendMessages.getFirst().getParameters().get("chat_id"));
        assertEquals(expectedText, sendMessages.getFirst().getParameters().get("text"));

        assertEquals(202L, sendMessages.get(1).getParameters().get("chat_id"));
        assertEquals(expectedText, sendMessages.get(1).getParameters().get("text"));

        assertEquals(303L, sendMessages.get(2).getParameters().get("chat_id"));
        assertEquals(expectedText, sendMessages.get(2).getParameters().get("text"));
    }

    @Test
    void sendUpdateMessage_withEmptyChat_dontInvokeBotExecute() {
        List<Long> chatIds = List.of();
        String expectedText = "New commit: http://1234.com";
        LinkUpdate linkUpdate = new LinkUpdate(1L, "http://1234.com", "New commit", chatIds);

        updateService.sendUpdateMessage(linkUpdate);

        verify(telegramBot, never()).execute(any(SendMessage.class));
    }*/
}
