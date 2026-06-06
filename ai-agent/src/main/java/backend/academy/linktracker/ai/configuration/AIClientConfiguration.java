package backend.academy.linktracker.ai.configuration;

import backend.academy.linktracker.ai.client.AIClient;
import backend.academy.linktracker.ai.properties.AIProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AIClientConfiguration {

    @Bean
    public AIClient aiClient(AIProperties properties) {
        RestClient client = RestClient.builder()
                .defaultHeader("Authorization", "Bearer %s".formatted(properties.token()))
                .baseUrl(properties.url())
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(AIClient.class);
    }
}
