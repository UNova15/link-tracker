package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.cache")
@Validated
public record CacheProperties(CacheConfigProperty valkey, CacheConfigProperty rateLimiter) {

    public record CacheConfigProperty(
            @NotNull @DurationUnit(ChronoUnit.HOURS) Duration ttl,
            @Positive int maxSize) {}
}
