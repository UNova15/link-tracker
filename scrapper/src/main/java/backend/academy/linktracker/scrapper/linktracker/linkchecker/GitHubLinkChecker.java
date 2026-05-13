package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.GitHubDto;
import backend.academy.linktracker.scrapper.dto.GitHubResponse;
import backend.academy.linktracker.scrapper.linksclient.GitHubClient;
import backend.academy.linktracker.scrapper.util.LinkParser;
import org.springframework.stereotype.Service;

@Service
public class GitHubLinkChecker extends LinkChecker {
    private final GitHubClient client;
    private final LinkParser parser;

    public GitHubLinkChecker(GitHubClient client, LinkParser parser) {
        super(LinkType.GIT_HUB);
        this.client = client;
        this.parser = parser;
    }

    @Override
    public boolean checkLink(Link link) {
        GitHubDto credentials = parser.parseGitHubLink(link.getUrl());
        GitHubResponse[] response =
                client.sendURequestForUpdates(credentials.owner(), credentials.repo(), link.getLastCheck());
        return isUpdatedAfterLastCheck(response);
    }

    private boolean isUpdatedAfterLastCheck(GitHubResponse[] response) {
        return response != null && response.length > 0;
    }
}
