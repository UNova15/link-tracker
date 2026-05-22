package unit.servicetest;

import static com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.linktracker.bot.telegramservice.BotUpdateListener;
import backend.academy.linktracker.bot.telegramservice.StateProcessor;
import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BotUpdateListenerTest {

    private final Gson gson = new Gson();

    @Mock
    private TelegramBot bot;

    @Mock
    private StateProcessor processor;

    @InjectMocks
    private BotUpdateListener botUpdateListener;

    private Update createUpdate(long chatId, String text) {
        String jsonTemplate = """
            {
              "update_id": 123456789,
              "message": {
                "message_id": 1001,
                "from": { "id": %d, "is_bot": false, "first_name": "Иван" },
                "chat": { "id": %d, "first_name": "Иван", "type": "private" },
                "date": 1678888888,
                "text": "%s"
              }
            }
            """;
        String finalJson = String.format(jsonTemplate, chatId, chatId, text);
        return gson.fromJson(finalJson, Update.class);
    }

    @Test
    void process_withValidUpdateMessage_executeCommand() {
        long chatId = 1;
        String userMessage = "Test";
        String response = "Ok";
        Update update = createUpdate(chatId, userMessage);
        List<Update> updates = List.of(update);

        when(processor.process(chatId, userMessage)).thenReturn(response);

        int actualStatusCode = botUpdateListener.process(updates);

        assertEquals(CONFIRMED_UPDATES_ALL, actualStatusCode);
        verify(processor, times(updates.size())).process(chatId, userMessage);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(updates.size())).execute(captor.capture());

        SendMessage message = captor.getValue();
        assertEquals(chatId, message.getChatId());
        assertEquals(response, message.getText());
    }

    @Test
    void process_withListOfValidUpdateMessage_executeCommand() {
        String userMessage = "Test";
        String response = "Ok";
        int size = 3;

        List<Update> updates = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Update update = createUpdate(i, userMessage);
            updates.add(update);
        }

        when(processor.process(any(Long.class), eq(userMessage))).thenReturn(response);

        int actualStatusCode = botUpdateListener.process(updates);

        assertEquals(CONFIRMED_UPDATES_ALL, actualStatusCode);
        verify(processor, times(updates.size())).process(any(Long.class), eq(userMessage));

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot, times(updates.size())).execute(captor.capture());

        List<SendMessage> messages = captor.getAllValues();
        for (int i = 0; i < size; i++) {
            assertEquals(i, messages.get(i).getChatId());
            assertEquals(response, messages.get(i).getText());
        }
    }
}
