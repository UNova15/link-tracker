package backend.academy.linktracker.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubUser(@JsonProperty("login") String login) {}
