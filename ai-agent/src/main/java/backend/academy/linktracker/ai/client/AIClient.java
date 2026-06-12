package backend.academy.linktracker.ai.client;

import static backend.academy.linktracker.ai.configuration.AIClientConfiguration.AI_AGENT_CONFIG;

import backend.academy.linktracker.ai.dto.AIRequest;
import backend.academy.linktracker.ai.dto.AIResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
@Retry(name = AI_AGENT_CONFIG)
public interface AIClient {

    @PostExchange
    AIResponse sendRequest(@RequestBody AIRequest requestMessage);
}
