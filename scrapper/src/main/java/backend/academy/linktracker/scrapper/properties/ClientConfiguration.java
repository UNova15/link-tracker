package backend.academy.linktracker.scrapper.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfiguration {
    private GithubProperties githubProperties;

    @Autowired
    public ClientConfiguration(GithubProperties githubProperties) {
        this.githubProperties = githubProperties;
    }

    @Bean
    public RestClient gitHubHttpClient() {
        return RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .baseUrl("https://api.github.com")
            .defaultHeader("Accept", "application/vnd.github+json")
            .defaultHeader("Authorization", "Bearer " + githubProperties.getToken())
            .build();
    }

    @Bean
    public RestClient stackOverflowHttpClient() {
        return RestClient.builder()
            .requestFactory(new HttpComponentsClientHttpRequestFactory())
            .baseUrl("https://api.stackexchange.com")
            .build();
    }
}
