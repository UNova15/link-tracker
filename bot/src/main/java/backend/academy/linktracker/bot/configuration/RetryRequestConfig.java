package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.exception.ScrapperApiException;
import backend.academy.linktracker.bot.exception.TelegramApiException;
import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResourceAccessException;
import java.net.SocketException;
import java.util.List;
import java.util.function.Predicate;

@Configuration
public class RetryRequestConfig {

    @Value("${app.retryable-statuses}")
    List<Integer> retryableStatuses;

    @Bean
    public RetryConfigCustomizer scrapperRetryConfigCustomizer() {
        return RetryConfigCustomizer.of("scrapper", builder -> {
            Predicate<Throwable> predicate = this::scrapperRetry;
            builder.retryOnException(predicate);
        });
    }

    @Bean
    public RetryConfigCustomizer telegramRetryConfigCustomizer() {
        return RetryConfigCustomizer.of("telegram", builder -> {
            Predicate<Throwable> predicate = this::telegramRetry;
            builder.retryOnException(predicate);
        });
    }

    private boolean telegramRetry(Throwable throwable) {
        if (throwable instanceof TelegramApiException exception) {
            return retryableStatuses.contains(exception.getErrorCode());
        }
        return throwable instanceof SocketException;
    }

    private boolean scrapperRetry(Throwable throwable) {
        if (throwable instanceof ResourceAccessException) {
            return true;
        }

        if (throwable instanceof ScrapperApiException exception) {
            return retryableStatuses.contains(exception.getCode().value());
        }
        return false;
    }
}
