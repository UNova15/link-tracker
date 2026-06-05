package backend.academy.linktracker.ai.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ai-config")
public record AIProperties(String url, String token, Query query, String model, long threshold) {

    public record Query(String role, String prompt) {}
}
