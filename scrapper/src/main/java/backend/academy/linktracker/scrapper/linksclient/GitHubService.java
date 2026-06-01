package backend.academy.linktracker.scrapper.linksclient;

import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GitHubService {
    private final GitHubClient gitHubClient;

    @Retry(name = "github")
    public List<GitHubResponse> sendURequestForUpdates(String owner, String repo, Instant lastUpdate) {
        String sinceTime = lastUpdate.truncatedTo(ChronoUnit.SECONDS).toString();

        return gitHubClient.sendURequestForUpdates(owner, repo, sinceTime);
    }
}
