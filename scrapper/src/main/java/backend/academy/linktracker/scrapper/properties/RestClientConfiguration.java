package backend.academy.linktracker.scrapper.properties;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@AllArgsConstructor
public class RestClientConfiguration {
    private final GithubProperties githubProperties;

    @Bean
    public RestClient gitHubHttpClient(GithubProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("Authorization", "Bearer " + githubProperties.getToken())
                .build();
    }

    @Bean
    public RestClient stackOverflowHttpClient(StackoverflowProperties properties) {
        return RestClient.builder().baseUrl(properties.getBaseUrl()).build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app",name = "sender", havingValue = "rest")
    public RestClient telegramBotHttpClient(TelegramBotProperties properties) {
        return RestClient.builder().baseUrl(properties.getBaseUrl()).build();
    }
}
