package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.properties.ScrapperClientProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@AllArgsConstructor
public class ScrapperClientConfiguration {
    private final ScrapperClientProperties properties;

    @Bean
    public RestClient scrapperClient() {
        return RestClient.builder().baseUrl(properties.getBaseUrl()).build();
    }
}
