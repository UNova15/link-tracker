package backend.academy.linktracker.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record GitHubResponse(
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("title") String title,
        @JsonProperty("body") String description,
        @JsonProperty("user") GitHubUser user) {}
