import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.service.BotUpdateListener;
import backend.academy.linktracker.bot.service.commands.HelpHandler;
import backend.academy.linktracker.bot.service.commands.StartHandler;
import backend.academy.linktracker.bot.service.commands.UnknownCommandHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UnitTests {

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private CommandRegistry registry;

    private BotUpdateListener listener;

    @Captor
    private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    @BeforeEach
    void setUp() {

        StartHandler startHandler = new StartHandler();
        HelpHandler helpHandler = new HelpHandler(registry);
        UnknownCommandHandler unknownCommandHandler = new UnknownCommandHandler();

        when(registry.getCommandHandlers()).thenReturn(List.of(startHandler, helpHandler));
        when(registry.getUnknownCommandHandler()).thenReturn(unknownCommandHandler);
        listener = new BotUpdateListener(telegramBot, registry);
    }

    @Test
    public void give_StartCommand_shouldReturnWelcomeMessage() {
        Update update = TestUtil.createUpdateWithMessage("/start", 1);

        listener.process(List.of(update));

        verify(telegramBot).execute(sendMessageArgumentCaptor.capture());
        SendMessage message = sendMessageArgumentCaptor.getValue();
        assertThat(message.getParameters().get("chat_id")).isEqualTo(1);
        assertThat(message.getParameters().get("text"))
                .isEqualTo("Добро пожаловать! Используйте /help, чтобы посмотреть доступные команды.");
    }

    @Test
    public void give_HelpCommand_shouldReturnListOfCommands() {
        Update update = TestUtil.createUpdateWithMessage("/help", 1);

        listener.process(List.of(update));

        verify(telegramBot).execute(sendMessageArgumentCaptor.capture());
        SendMessage message = sendMessageArgumentCaptor.getValue();
        assertThat(message.getParameters().get("chat_id")).isEqualTo(1);
        String text = (String) message.getParameters().get("text");
        assertThat(text).contains("/start", "/help");
    }

    @Test
    public void give_InvalidCommand_shouldReturnErrorMessage() {
        Update update = TestUtil.createUpdateWithMessage("/AAAAA", 1);

        listener.process(List.of(update));

        verify(telegramBot).execute(sendMessageArgumentCaptor.capture());
        SendMessage message = sendMessageArgumentCaptor.getValue();
        assertThat(message.getParameters().get("chat_id")).isEqualTo(1);
        assertThat(message.getParameters().get("text"))
                .isEqualTo("Неизвестная команда. Воспользуйтесь /help, чтобы посмотреть список доступных команд.");
    }
}
