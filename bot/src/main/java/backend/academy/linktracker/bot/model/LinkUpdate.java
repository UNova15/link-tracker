package backend.academy.linktracker.bot.model;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record LinkUpdate(long id, @URL String url, @NotNull String description, @NotNull long[] tgChatIds) {
}
