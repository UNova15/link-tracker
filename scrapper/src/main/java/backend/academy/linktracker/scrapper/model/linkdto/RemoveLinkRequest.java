package backend.academy.linktracker.scrapper.model.linkdto;

import jakarta.validation.constraints.NotNull;

public record RemoveLinkRequest(@NotNull(message = "Пустая ссылка") String link) {
}
