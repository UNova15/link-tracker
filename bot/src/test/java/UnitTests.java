import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.client.telegram.BotUpdateListener;
import backend.academy.linktracker.bot.handler.command.HelpHandler;
import backend.academy.linktracker.bot.handler.command.StartHandler;
import backend.academy.linktracker.bot.handler.command.UnknownCommandHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.Optional;
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

    private StartHandler startHandler;
    private UnknownCommandHandler unknownCommandHandler;
    private HelpHandler helpHandler;

    @Captor
    private ArgumentCaptor<SendMessage> sendMessageArgumentCaptor;

    @BeforeEach
    void setUp() {

        startHandler = new StartHandler();
        unknownCommandHandler = new UnknownCommandHandler();
        helpHandler = new HelpHandler(registry);

        lenient().when(registry.getCommandHandlers()).thenReturn(List.of(startHandler, helpHandler));
        lenient().when(registry.getUnknownCommandHandler()).thenReturn(unknownCommandHandler);
        lenient().when(registry.getCommandHandler(anyString())).thenReturn(Optional.empty());
        listener = new BotUpdateListener(telegramBot, registry);
    }

    @Test
    public void give_StartCommand_shouldReturnWelcomeMessage() {
        when(registry.getCommandHandler("/start")).thenReturn(Optional.of(startHandler));
        Update update = TestUtil.createUpdateWithMessage("/start", 1);

        listener.process(List.of(update));

        verify(telegramBot).execute(sendMessageArgumentCaptor.capture());
        SendMessage message = sendMessageArgumentCaptor.getValue();
        assertThat(message.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(message.getParameters().get("text"))
                .isEqualTo("Добро пожаловать! Используйте /help, чтобы посмотреть доступные команды.");
    }

    @Test
    public void give_HelpCommand_shouldReturnListOfCommands() {
        when(registry.getCommandHandler("/help")).thenReturn(Optional.of(helpHandler));
        Update update = TestUtil.createUpdateWithMessage("/help", 1);

        listener.process(List.of(update));

        verify(telegramBot).execute(sendMessageArgumentCaptor.capture());
        SendMessage message = sendMessageArgumentCaptor.getValue();
        assertThat(message.getParameters().get("chat_id")).isEqualTo(1L);
        String text = (String) message.getParameters().get("text");
        assertThat(text).contains("/start", "/help");
    }

    @Test
    public void give_InvalidCommand_shouldReturnErrorMessage() {
        Update update = TestUtil.createUpdateWithMessage("/AAAAA", 1);

        listener.process(List.of(update));

        verify(telegramBot).execute(sendMessageArgumentCaptor.capture());
        SendMessage message = sendMessageArgumentCaptor.getValue();
        assertThat(message.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(message.getParameters().get("text"))
                .isEqualTo("Неизвестная команда. Воспользуйтесь /help, чтобы посмотреть список доступных команд.");
    }
}
