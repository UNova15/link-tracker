package backend.academy.linktracker.ai.properties;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.grouping")
@Validated
public record GroupingProperties(@NotNull Duration windowMs) {}
