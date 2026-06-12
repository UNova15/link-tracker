package backend.academy.linktracker.scrapper.linksclient;

import static backend.academy.linktracker.scrapper.configuration.HttpClientConfiguration.GIT_HUB_CONFIG_NAME;

import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/repos/{owner}/{repo}/issues")
@Retry(name = GIT_HUB_CONFIG_NAME)
@CircuitBreaker(name = GIT_HUB_CONFIG_NAME)
public interface GitHubClient {

    @GetExchange
    List<GitHubResponse> sendURequestForUpdates(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @RequestParam("since") String sinceTime);
}
