package backend.academy.linktracker.scrapper.linksclient;

import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import java.util.List;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/repos/{owner}/{repo}/issues")
public interface GitHubClient {

    @GetExchange
    List<GitHubResponse> sendURequestForUpdates(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @RequestParam("since") String sinceTime);
}
