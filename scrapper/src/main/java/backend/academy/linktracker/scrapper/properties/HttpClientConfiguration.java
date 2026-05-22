package backend.academy.linktracker.scrapper.properties;

import backend.academy.linktracker.scrapper.exception.handler.TelegramBotExceptionHandler;
import backend.academy.linktracker.scrapper.linksclient.GitHubClient;
import backend.academy.linktracker.scrapper.linksclient.StackOverflowClient;
import backend.academy.linktracker.scrapper.messagesender.TelegramBotClient;
import java.net.URI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class HttpClientConfiguration {

    @Bean
    public GitHubClient gitHubHttpClient(GithubProperties properties) {
        ClientHttpRequestInterceptor defaultParamsInterceptor = (request, body, execution) -> {
            URI newUri = UriComponentsBuilder.fromUri(request.getURI())
                    .queryParam("sort", "created")
                    .queryParam("direction", "desc")
                    .build()
                    .toUri();

            HttpRequest newRequest = new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    return newUri;
                }
            };

            return execution.execute(newRequest, body);
        };

        RestClient client = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .requestInterceptor(defaultParamsInterceptor)
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("Authorization", "Bearer " + properties.getToken())
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(GitHubClient.class);
    }

    @Bean
    public StackOverflowClient stackOverflowHttpClient(StackoverflowProperties properties) {

        ClientHttpRequestInterceptor defaultParamsInterceptor = (request, body, execution) -> {
            URI newUri = UriComponentsBuilder.fromUri(request.getURI())
                    .queryParam("site", "stackoverflow")
                    .queryParam("filter", "!nKzQUR3E_f")
                    .queryParam("key", properties.getKey())
                    .build()
                    .toUri();

            HttpRequest newRequest = new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    return newUri;
                }
            };
            return execution.execute(newRequest, body);
        };

        RestClient client = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .requestInterceptor(defaultParamsInterceptor)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(StackOverflowClient.class);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "sender", havingValue = "http")
    public TelegramBotClient telegramBotHttpClient(
            TelegramBotProperties properties, TelegramBotExceptionHandler handler) {
        RestClient client = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, handler::handleTelegramError)
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, handler::handleTelegramError)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(TelegramBotClient.class);
    }
}
