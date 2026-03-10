package backend.academy.linktracker.scrapper.linksclient;

import backend.academy.linktracker.scrapper.model.GitHubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.time.Instant;

@Component
public class GitHubClient {
    private RestClient gitHubClient;

    @Autowired
    public GitHubClient(@Qualifier("gitHubHttpClient") RestClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    //Проверка обновления issues в github
    public GitHubResponse[] sendURequestForUpdates(String owner, String repo, Instant lastCheck) {
        return gitHubClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/repos/{owner}/{repo}/issues")
                .queryParam("since", lastCheck.toString())
                .queryParam("sort", "updated")
                .queryParam("direction", "desc")
                .queryParam("per_page", 1)
                .build(owner, repo))
            .retrieve()
            .body(GitHubResponse[].class);
    }

}
