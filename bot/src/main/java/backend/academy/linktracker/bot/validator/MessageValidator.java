package backend.academy.linktracker.bot.validator;

import backend.academy.linktracker.bot.service.commands.CommandHandler;
import com.pengrad.telegrambot.model.Message;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MessageValidator {
    public boolean isValidMessage(Message message, List<CommandHandler> handlers) {
        return message != null
            && message.text() != null
            && isValidCommandName(message.text(),handlers);
    }

    private boolean isValidCommandName(String command, List<CommandHandler> handlers) {
        boolean isContains = false;
        for (CommandHandler handler : handlers) {
            if (handler.getName().equals(command)) {
                isContains = true;
                break;
            }
        }
        return isContains;
    }
}
