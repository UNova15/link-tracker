package backend.academy.linktracker.scrapper.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfiguration {
    private final GithubProperties githubProperties;

    @Autowired
    public ClientConfiguration(GithubProperties githubProperties) {
        this.githubProperties = githubProperties;
    }

    @Bean
    public RestClient gitHubHttpClient(GithubProperties properties) {
        return RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .baseUrl(properties.getBaseUrl())
            .defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("Authorization", "Bearer " + githubProperties.getToken())
            .build();
    }

    @Bean
    public RestClient stackOverflowHttpClient(StackoverflowProperties properties) {
        return RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .baseUrl(properties.getBaseUrl())
            .build();
    }

    @Bean
    public RestClient telegramBotHttpClient(TelegramBotProperties properties) {
        return RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .baseUrl(properties.getBaseUrl())
            .build();
    }
}
