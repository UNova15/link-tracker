package backend.academy.linktracker.scrapper.linksclient;

import backend.academy.linktracker.scrapper.dto.GitHubResponse;
import backend.academy.linktracker.scrapper.properties.GithubProperties;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GitHubClient {
    private final RestClient gitHubClient;
    private final GithubProperties properties;

    public GitHubClient(@Qualifier("gitHubHttpClient") RestClient gitHubClient, GithubProperties properties) {
        this.gitHubClient = gitHubClient;
        this.properties = properties;
    }

    // Проверка обновления issues в github
    public GitHubResponse[] sendURequestForUpdates(String owner, String repo, Instant lastCheck) {
        return gitHubClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(properties.getIssuesEndpoint())
                        .queryParam("since", lastCheck.toString())
                        .queryParam("sort", "updated")
                        .queryParam("direction", "desc")
                        .queryParam("per_page", 1)
                        .build(owner, repo))
                .retrieve()
                .body(GitHubResponse[].class);
    }
}
