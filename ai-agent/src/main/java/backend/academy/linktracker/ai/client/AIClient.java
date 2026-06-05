package backend.academy.linktracker.ai.client;

import backend.academy.linktracker.ai.dto.AIRequest;
import backend.academy.linktracker.ai.dto.AIResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface AIClient {

    @PostExchange
    AIResponse sendRequest(@RequestBody AIRequest requestMessage);
}
