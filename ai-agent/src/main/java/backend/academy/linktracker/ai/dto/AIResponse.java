package backend.academy.linktracker.ai.dto;

import java.util.List;

public record AIResponse(List<Choice> choices) {
    public record Choice(Message message) {}

    public record Message(String content) {}
}
