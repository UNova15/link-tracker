package backend.academy.linktracker.scrapper.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StackOverflowQuestion(
        @JsonProperty("title") String title,
        @JsonProperty("answers") List<StackOverflowContent> answers,
        @JsonProperty("comments") List<StackOverflowContent> comments) {}
