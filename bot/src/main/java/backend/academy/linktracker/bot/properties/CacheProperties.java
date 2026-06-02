package backend.academy.linktracker.bot.properties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties(prefix = "app.cache")
@Validated
public record CacheProperties(
        @NotNull CacheConfigProperties rateLimiter, @NotNull CacheConfigProperties idempotencyKey) {

    public record CacheConfigProperties(
            @Positive long maxSize,
            @Positive @DurationUnit(ChronoUnit.HOURS) Duration ttl) {}
}
