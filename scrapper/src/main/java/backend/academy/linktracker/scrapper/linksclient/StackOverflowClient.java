package backend.academy.linktracker.scrapper.linksclient;

import backend.academy.linktracker.scrapper.model.StackOverflowQuestion;
import backend.academy.linktracker.scrapper.model.StackOverflowResponse;
import backend.academy.linktracker.scrapper.properties.StackoverflowProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.time.Instant;

@Component
public class StackOverflowClient {
    private StackoverflowProperties properties;
    private RestClient stackOverflowClient;

    @Autowired
    public StackOverflowClient(@Qualifier("stackOverflowHttpClient") RestClient stackOverflowClient, StackoverflowProperties properties) {
        this.stackOverflowClient = stackOverflowClient;
        this.properties = properties;
    }

    //Проверка изменения состояния вопроса на stackoverflow
    public boolean hasActivity(long questionId, Instant lastCheck) {
        var response = stackOverflowClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/questions/{id}")
                .queryParam("site", "stackoverflow")
                .queryParam("key", properties.getKey())
                .build(questionId))
            .retrieve()
            .body(new ParameterizedTypeReference<StackOverflowResponse<StackOverflowQuestion>>() {
            });

        return isUpdatedAfterLastCheck(response, lastCheck);
    }

    private boolean isUpdatedAfterLastCheck(StackOverflowResponse<StackOverflowQuestion> response, Instant lastCheck) {
        return response != null
            && response.items() != null
            && !response.items().isEmpty()
            && Instant.ofEpochSecond(response.items().getFirst().lastActivityDate()).isAfter(lastCheck);
    }
}
