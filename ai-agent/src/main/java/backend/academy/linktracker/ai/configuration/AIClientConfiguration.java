package backend.academy.linktracker.ai.configuration;

import backend.academy.linktracker.ai.client.AIClient;
import backend.academy.linktracker.ai.properties.AIProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AIClientConfiguration {
    public static final String AI_AGENT_CONFIG = "ai-agent";

    @Bean
    public AIClient aiClient(AIProperties properties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(properties.readTimeout());
        requestFactory.setConnectTimeout(properties.connectionTimeout());

        RestClient client = RestClient.builder()
                .defaultHeader("Authorization", "Bearer %s".formatted(properties.token()))
                .baseUrl(properties.url())
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory proxyFactory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return proxyFactory.createClient(AIClient.class);
    }
}
