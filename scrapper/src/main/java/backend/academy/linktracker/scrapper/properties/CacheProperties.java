package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.cache")
@Validated
public record CacheProperties(
        @PositiveOrZero int ttl, @PositiveOrZero int maxSizeLocalCache) {}
