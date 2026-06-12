package backend.academy.linktracker.bot.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.cache")
@Validated
public record CacheProperties(
        @NotNull @Valid CacheConfigProperties rateLimiter,
        @NotNull @Valid CacheConfigProperties idempotencyKey) {

    public record CacheConfigProperties(
            @Positive long maxSize, @NotNull Duration ttl) {}
}
