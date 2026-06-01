package backend.academy.linktracker.scrapper.configuration;

import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import java.util.List;
import java.util.function.Predicate;

@Configuration
public class RetryRequestConfig {

    @Value("${app.retryable-statuses}")
    List<Integer> retryableStatuses;

    @Bean
    public RetryConfigCustomizer gitHubRetryConfigCustomizer() {
        return RetryConfigCustomizer.of("github", builder -> {
            Predicate<Throwable> predicate = this::shouldRetry;
            builder.retryOnException(predicate);
        });
    }

    @Bean
    public RetryConfigCustomizer stackOverflowConfigCustomizer() {
        return RetryConfigCustomizer.of("stackoverflow", builder -> {
            Predicate<Throwable> predicate = this::shouldRetry;
            builder.retryOnException(predicate);
        });
    }

    @Bean
    public RetryConfigCustomizer botConfigCustomizer() {
        return RetryConfigCustomizer.of("bot", builder -> {
            Predicate<Throwable> predicate = this::shouldRetry;
            builder.retryOnException(predicate);
        });
    }

    private boolean shouldRetry(Throwable throwable) {
        if (throwable instanceof ResourceAccessException) {
            return true;
        }

        if (throwable instanceof HttpStatusCodeException exception) {
            return retryableStatuses.contains(exception.getStatusCode().value());
        }
        return false;
    }
}
