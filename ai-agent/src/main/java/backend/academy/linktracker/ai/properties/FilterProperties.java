package backend.academy.linktracker.ai.properties;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.filter")
@Validated
public record FilterProperties(
        @NotEmpty List<String> stopWords,
        @NotEmpty List<String> excludedAuthors,
        @Positive long minLength) {}
