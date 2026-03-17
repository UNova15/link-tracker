package backend.academy.linktracker.scrapper.linksclient;

import backend.academy.linktracker.scrapper.model.StackOverflowQuestion;
import backend.academy.linktracker.scrapper.model.StackOverflowResponse;
import backend.academy.linktracker.scrapper.properties.StackoverflowProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class StackOverflowClient {
    private final StackoverflowProperties properties;
    private final RestClient stackOverflowClient;

    @Autowired
    public StackOverflowClient(
            @Qualifier("stackOverflowHttpClient") RestClient stackOverflowClient, StackoverflowProperties properties) {
        this.stackOverflowClient = stackOverflowClient;
        this.properties = properties;
    }

    // Проверка изменения состояния вопроса на stackoverflow
    public StackOverflowResponse<StackOverflowQuestion> sendURequestForUpdates(long questionId) {
        return stackOverflowClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(properties.getQuestionEndpoint())
                        .queryParam("site", "stackoverflow")
                        .queryParam("key", properties.getKey())
                        .build(questionId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
