package backend.academy.linktracker.ai.properties;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.grouping")
@Validated
public record GroupingProperties(@PositiveOrZero long windowMs) {}
