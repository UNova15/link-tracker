package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.linksclient.GitHubClient;
import backend.academy.linktracker.scrapper.model.GitHubDto;
import backend.academy.linktracker.scrapper.model.GitHubResponse;
import backend.academy.linktracker.scrapper.model.linkdto.Link;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.parser.LinkParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubLinkChecker extends LinkChecker {
    private final GitHubClient client;
    private final LinkParser parser;

    @Autowired
    public GitHubLinkChecker(GitHubClient client, LinkParser parser) {
        super(LinkType.GIT_HUB);
        this.client = client;
        this.parser = parser;
    }

    @Override
    public boolean checkLink(Link link) {
        GitHubDto credentials = parser.parseGitHubLink(link.url());
        GitHubResponse[] response = client.sendURequestForUpdates(credentials.owner(), credentials.repo(), link.lastCheck());
        return isUpdatedAfterLastCheck(response);
    }

    private boolean isUpdatedAfterLastCheck(GitHubResponse[] response) {
        return response != null && response.length > 0;
    }

}
