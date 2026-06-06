package backend.academy.linktracker.ai.properties;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.filter-config")
public record FilterProperties(List<String> stopWords, List<String> excludedAuthors, long minLength) {}
