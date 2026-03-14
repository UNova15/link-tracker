package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.model.BotState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class SessionData {
    private BotState state;
    private String link;
    private List<String> tags;

    public SessionData(){
        this.state = BotState.NEW;
    }
}
