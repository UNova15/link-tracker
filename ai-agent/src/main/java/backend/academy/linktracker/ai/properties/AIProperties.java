package backend.academy.linktracker.ai.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.ai")
@Validated
public record AIProperties(
        @NotBlank String url,
        @NotBlank String token,
        @NotNull Query query,
        @NotBlank String model,
        @Positive long threshold) {

    public record Query(@NotBlank String role, @NotBlank String prompt) {}
}
