package backend.academy.linktracker.scrapper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubResponse(
    @JsonProperty("updated_at") String updatedAt,
    @JsonProperty("title") String title){
}
