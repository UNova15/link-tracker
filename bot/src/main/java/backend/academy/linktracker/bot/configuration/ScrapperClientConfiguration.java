package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.exception.ScrapperErrorHandler;
import backend.academy.linktracker.bot.properties.ScrapperProperties;
import backend.academy.linktracker.bot.client.ScrapperChatClient;
import backend.academy.linktracker.bot.client.ScrapperLinkClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ScrapperClientConfiguration {

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(
            ScrapperProperties properties, ScrapperErrorHandler errorHandler) {

        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(properties.getConnectionTimeout());
        httpRequestFactory.setReadTimeout(properties.getResponseTimeout());

        RestClient restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .requestFactory(httpRequestFactory)
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, errorHandler::handleScrapperError)
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, errorHandler::handleScrapperError)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory.builderFor(adapter).build();
    }

    @Bean
    public ScrapperChatClient scrapperChatClient(HttpServiceProxyFactory factory) {
        return factory.createClient(ScrapperChatClient.class);
    }

    @Bean
    public ScrapperLinkClient scrapperLinkClient(HttpServiceProxyFactory factory) {
        return factory.createClient(ScrapperLinkClient.class);
    }
}
