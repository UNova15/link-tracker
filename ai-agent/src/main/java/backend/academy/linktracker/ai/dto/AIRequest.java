package backend.academy.linktracker.ai.dto;

import java.util.List;

public record AIRequest(List<Message> messages, String model) {
    public record Message(String role, String content) {}
}
