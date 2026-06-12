package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.cache")
@Validated
public record CacheProperties(
        @NotNull Duration ttl, @Positive int maxSize) {}
