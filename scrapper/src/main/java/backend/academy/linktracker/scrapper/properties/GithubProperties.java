package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.github")
@Validated
public record GithubProperties(
        @NotBlank String token,
        @NotBlank String baseUrl,
        @PositiveOrZero int connectionTimeout,
        @PositiveOrZero int responseTimeout) {}
