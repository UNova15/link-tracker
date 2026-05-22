package backend.academy.linktracker.scrapper.linksclient;

import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowResponse;
import backend.academy.linktracker.scrapper.properties.StackoverflowProperties;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
//TODO переписать в декларативном стиле
@Component
public class StackOverflowClient {
    private final StackoverflowProperties properties;
    private final RestClient stackOverflowClient;

    public StackOverflowClient(
            @Qualifier("stackOverflowHttpClient") RestClient stackOverflowClient, StackoverflowProperties properties) {
        this.stackOverflowClient = stackOverflowClient;
        this.properties = properties;
    }

    // Проверка изменения состояния вопроса на stackoverflow
    public StackOverflowResponse sendURequestForUpdates(long questionId, Instant lastCheck) {
        return stackOverflowClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/questions/{id}")
                        .queryParam("site", "stackoverflow")
                        .queryParam("key", properties.getKey())
                        .queryParam("filter", "!nKzQUR3E_f")
                        .queryParam("fromdate", lastCheck.getEpochSecond())
                        .build(questionId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
