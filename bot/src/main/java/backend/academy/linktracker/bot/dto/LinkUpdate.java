package backend.academy.linktracker.bot.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.hibernate.validator.constraints.URL;

public record LinkUpdate(
        long id,
        @URL String url,
        @NotNull String description,
        @NotNull List<Long> tgChatIds) {}
