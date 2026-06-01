package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.scrapper-settings")
public record ScrapperProperties(
        @PositiveOrZero int ageOfLinks,
        @PositiveOrZero int numberOfThreads,
        @PositiveOrZero int batchSize) {}
