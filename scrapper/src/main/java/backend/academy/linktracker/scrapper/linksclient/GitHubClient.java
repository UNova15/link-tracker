package backend.academy.linktracker.scrapper.linksclient;

import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GitHubClient {
    private final RestClient gitHubClient;

    public GitHubClient(@Qualifier("gitHubHttpClient") RestClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    // Проверка обновления issues в github
    public List<GitHubResponse> sendURequestForUpdates(String owner, String repo, Instant lastCheck) {

        return gitHubClient
                .get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/repos/{owner}/{repo}/issues")
                        .queryParam("sort", "created")
                        .queryParam("direction", "desc");

                    if(lastCheck!=null) {
                        uriBuilder.queryParam("since",lastCheck.truncatedTo(ChronoUnit.SECONDS).toString());
                    }
                    return uriBuilder.build(owner, repo);
                })
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
