package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.scrapper-settings")
public record ScrapperProperties(
        @NotNull Duration ageOfLinks,
        @PositiveOrZero int numberOfThreads,
        @PositiveOrZero int batchSize) {}
