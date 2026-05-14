package backend.academy.linktracker.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubResponse(
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("title") String title,
        @JsonProperty("body") String description,
        @JsonProperty("user") GitHubUser user) {}
