package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.github")
@Validated
public record GithubProperties(
        @NotBlank String token,
        @NotBlank String baseUrl,
        @NotNull Duration connectionTimeout,
        @NotNull Duration responseTimeout) {}
