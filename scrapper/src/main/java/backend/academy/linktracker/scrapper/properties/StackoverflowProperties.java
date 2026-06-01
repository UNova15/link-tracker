package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.stackoverflow")
@Validated
public record StackoverflowProperties(
        @NotEmpty String key,
        @NotEmpty String accessToken,
        @NotEmpty String baseUrl,
        @PositiveOrZero int connectionTimeout,
        @PositiveOrZero int responseTimeout) {}
