package backend.academy.linktracker.ai.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

@ConfigurationProperties(prefix = "app.filter-config")
public record FilterProperties(List<String> stopWords, List<String> excludedAuthors, long minLength) {
}
