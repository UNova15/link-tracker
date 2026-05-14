package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.github.IssueCredential;
import backend.academy.linktracker.scrapper.linksclient.GitHubClient;
import backend.academy.linktracker.scrapper.util.LinkParser;
import backend.academy.linktracker.scrapper.util.ResponseFormatter;
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
    public Optional<String> check(Link link) {
        IssueCredential credentials = parser.parseGitHubLink(link.getUrl());
        List<GitHubResponse> response =
                client.sendURequestForUpdates(credentials.owner(), credentials.repo(), link.getLastCheck());

        if (response != null && !response.isEmpty()) {
            String formattedResponse = formatter.formatGitHubResponse(response);
            return Optional.of(formattedResponse);
        }
        return Optional.empty();
    }
}
