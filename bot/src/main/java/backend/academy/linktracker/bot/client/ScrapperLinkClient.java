package backend.academy.linktracker.bot.client;

import static backend.academy.linktracker.bot.configuration.ScrapperClientConfiguration.SCRAPPER_CONFIG_NAME;

import backend.academy.linktracker.bot.dto.AddLinkRequest;
import backend.academy.linktracker.bot.dto.LinkResponse;
import backend.academy.linktracker.bot.dto.ListLinkResponse;
import backend.academy.linktracker.bot.dto.RemoveLinkRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "/links")
@Retry(name = SCRAPPER_CONFIG_NAME)
@CircuitBreaker(name = SCRAPPER_CONFIG_NAME)
public interface ScrapperLinkClient {

    @GetExchange
    ListLinkResponse getLinks(@RequestHeader("Tg-Chat-Id") long id);

    @PostExchange
    LinkResponse addLink(@RequestHeader("Tg-Chat-Id") long id, @RequestBody AddLinkRequest req);

    @DeleteExchange
    LinkResponse removeLink(@RequestHeader("Tg-Chat-Id") long id, @RequestBody RemoveLinkRequest req);
}
