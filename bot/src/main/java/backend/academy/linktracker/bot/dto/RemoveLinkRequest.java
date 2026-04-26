package backend.academy.linktracker.bot.dto;

import jakarta.validation.constraints.NotNull;

public record RemoveLinkRequest(
        @NotNull(message = "Пустая ссылка") String link) {}
