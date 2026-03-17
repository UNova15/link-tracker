package backend.academy.linktracker.bot.model;

import backend.academy.linktracker.bot.state.State;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SessionData {
    private State state;
    private String link;
    private List<String> tags;

    public SessionData(State state) {
        this.state = state;
    }
}
