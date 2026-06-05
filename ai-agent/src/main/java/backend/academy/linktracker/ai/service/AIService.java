package backend.academy.linktracker.ai.service;

import backend.academy.linktracker.ai.client.AIClient;
import backend.academy.linktracker.ai.dto.AIRequest;
import backend.academy.linktracker.ai.dto.AIResponse;
import backend.academy.linktracker.ai.properties.AIProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class AIService {
    private final AIProperties properties;
    private final AIClient client;

    public String summarizing(String message) {
        if (message.length() <= properties.threshold()) {
            return message;
        }
        String aiMessage = properties.query().prompt().concat(message);

        AIRequest request =
                new AIRequest(List.of(new AIRequest.Message(properties.query().role(), aiMessage)), properties.model());

        AIResponse response = client.sendRequest(request);
        return response.choices().getFirst().message().content();
    }
}
