package backend.academy.linktracker.ai.properties;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@ConfigurationProperties(prefix = "app.prioritization")
@Validated
public record PrioritizationProperties(
        @NotEmpty List<String> highKeywords, @NotEmpty List<String> lowKeywords) {}
