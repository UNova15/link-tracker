package backend.academy.linktracker.scrapper.linksclient;

import static backend.academy.linktracker.scrapper.configuration.HttpClientConfiguration.STACK_OVERLOW_CONFIG_NAME;

import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/questions/{id}")
@Retry(name = STACK_OVERLOW_CONFIG_NAME)
@CircuitBreaker(name = STACK_OVERLOW_CONFIG_NAME)
public interface StackOverflowClient {

    @GetExchange
    StackOverflowResponse sendURequestForUpdates(@PathVariable("id") long id, @RequestParam("fromdate") long lastCheck);
}
