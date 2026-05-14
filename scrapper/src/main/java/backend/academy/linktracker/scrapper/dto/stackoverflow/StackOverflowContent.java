package backend.academy.linktracker.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowContent(
        @JsonProperty("owner") StackOverflowUser user,
        @JsonProperty("body") String body,
        @JsonProperty("creation_date") long creationDate) {}
