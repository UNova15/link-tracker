package backend.academy.linktracker.ai.service;

import static backend.academy.linktracker.ai.configuration.AIClientConfiguration.AI_AGENT_CONFIG;

import backend.academy.linktracker.ai.client.AIClient;
import backend.academy.linktracker.ai.dto.AIRequest;
import backend.academy.linktracker.ai.dto.AIResponse;
import backend.academy.linktracker.ai.properties.AIProperties;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AIService {
    private final AIProperties properties;
    private final AIClient client;

    @CircuitBreaker(name = AI_AGENT_CONFIG, fallbackMethod = "summarizingFallback")
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

    String summarizingFallback(String massage, Throwable throwable) {
        log.error("Ошибка отправки сообщения: {} ошибка: {}", massage, throwable.getMessage());

        return massage.substring(0, properties.threshold()).concat("...");
    }
}
