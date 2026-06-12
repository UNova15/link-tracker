package backend.academy.linktracker.ai.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.ai")
@Validated
public record AIProperties(
        @NotBlank String url,
        @NotBlank String token,
        @NotNull Query query,
        @NotBlank String model,
        @Positive int threshold,
        @NotNull Duration connectionTimeout,
        @NotNull Duration readTimeout) {

    public record Query(@NotBlank String role, @NotBlank String prompt) {}
}
