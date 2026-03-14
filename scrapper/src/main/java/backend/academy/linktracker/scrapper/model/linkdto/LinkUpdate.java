package backend.academy.linktracker.scrapper.model.linkdto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import java.util.List;

public record LinkUpdate(long id, @URL String url, @NotNull String description, @NotNull List<Long> tgChatIds) {
}
