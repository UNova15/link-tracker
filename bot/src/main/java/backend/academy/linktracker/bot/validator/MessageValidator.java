package backend.academy.linktracker.bot.validator;

import backend.academy.linktracker.bot.service.commands.CommandService;
import com.pengrad.telegrambot.model.Message;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MessageValidator {
    public boolean isValidMessage(Message message, List<CommandService> handlers) {
        return message != null
            && message.text() != null
            && isValidCommandName(message.text(),handlers);
    }

    private boolean isValidCommandName(String command, List<CommandService> handlers) {
        boolean isContains = false;
        for (CommandService handler : handlers) {
            if (handler.getCommandName().equals(command)) {
                isContains = true;
                break;
            }
        }
        return isContains;
    }
}
