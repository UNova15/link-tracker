package backend.academy.linktracker.scrapper.configuration;

import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import java.util.List;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

@Configuration
@Slf4j
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
            log.info("Ошибка доступа к ресурсу: {}", throwable.getLocalizedMessage());
            return true;
        }

        if (throwable instanceof HttpStatusCodeException exception) {
            log.info("Ошибка запроса к ресурсу: {}", throwable.getLocalizedMessage());
            return retryableStatuses.contains(exception.getStatusCode().value());
        }
        return false;
    }
}
