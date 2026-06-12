package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.bot")
@Validated
public record TelegramBotProperties(
        @NotEmpty String baseUrl,
        @NotNull Duration connectionTimeout,
        @NotNull Duration responseTimeout) {}
