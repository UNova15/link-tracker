package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.properties.ScrapperClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ScrapperClientConfiguration {
    private final ScrapperClientProperties properties;

    @Autowired
    public ScrapperClientConfiguration(ScrapperClientProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestClient scrapperClient() {
        return RestClient.builder()
            .baseUrl(properties.getBaseUrl())
            .build();
    }
}
