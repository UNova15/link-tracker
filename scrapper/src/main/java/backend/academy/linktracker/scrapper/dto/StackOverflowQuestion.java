package backend.academy.linktracker.scrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowQuestion(
        @JsonProperty("question_id") int questionId,
        @JsonProperty("title") String title,
        @JsonProperty("last_activity_date") long lastActivityDate,
        @JsonProperty("creation_date") long creationDate,
        @JsonProperty("is_answered") boolean isAnswered,
        @JsonProperty("answer_count") int answerCount) {}
