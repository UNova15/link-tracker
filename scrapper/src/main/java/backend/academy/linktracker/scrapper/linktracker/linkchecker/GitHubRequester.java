package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.github.IssueCredential;
import backend.academy.linktracker.scrapper.dto.linkdto.CheckResult;
import backend.academy.linktracker.scrapper.linksclient.GitHubClient;
import backend.academy.linktracker.scrapper.util.LinkParser;
import backend.academy.linktracker.scrapper.util.ResponseFormatter;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GitHubRequester extends ResourceRequester {
    private final GitHubClient client;
    private final ResponseFormatter formatter;
    private final LinkParser parser;

    public GitHubRequester(GitHubClient client, LinkParser parser, ResponseFormatter formatter) {
        super(LinkType.GIT_HUB);
        this.client = client;
        this.parser = parser;
        this.formatter = formatter;
    }

    @Override
    public Optional<CheckResult> check(Link link) {
        IssueCredential credentials = parser.parseGitHubLink(link.getUrl());

        List<GitHubResponse> response =
            client.sendURequestForUpdates(credentials.owner(), credentials.repo(), link.getLastUpdate());

        if (response == null || response.isEmpty()) {
            return Optional.empty();
        }

        Instant since = link.getLastUpdate() != null ? link.getLastUpdate() : link.getLastCheck();
        List<GitHubResponse> filtered = filterContentByCreationDate(response, since);

        if (filtered.isEmpty()) {
            return Optional.empty();
        }

        String formattedResponse = formatter.formatGitHubResponse(filtered);

        return Optional.of(new CheckResult(formattedResponse, findMaxUpdatedTime(filtered).orElse(null)));
    }

    private List<GitHubResponse> filterContentByCreationDate(List<GitHubResponse> changes, Instant since) {
        return changes.stream()
            .filter(change -> change.updatedAt().isAfter(since))
            .toList();
    }

    private Optional<Instant> findMaxUpdatedTime(List<GitHubResponse> changes) {
        return changes.stream()
            .map(GitHubResponse::updatedAt)
            .max(Instant::compareTo);
    }
}
