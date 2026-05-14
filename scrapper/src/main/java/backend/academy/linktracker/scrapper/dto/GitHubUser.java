package backend.academy.linktracker.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubUser(@JsonProperty("login") String login) {
}
