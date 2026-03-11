package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.linksclient.GitHubClient;
import backend.academy.linktracker.scrapper.model.GitHubDto;
import backend.academy.linktracker.scrapper.model.GitHubResponse;
import backend.academy.linktracker.scrapper.model.linkdto.LinkDto;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.parser.LinkParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GitHubLinkChecker extends LinkChecker {
    private GitHubClient client;
    private LinkParser parser;

    @Autowired
    public GitHubLinkChecker(GitHubClient client, LinkParser parser) {
        super(LinkType.GIT_HUB);
        this.client = client;
        this.parser = parser;
    }

    @Override
    public boolean checkLink(LinkDto linkDto) {
        GitHubDto credentials = parser.parseGitHubLink(linkDto.url());
        GitHubResponse[] response = client.sendURequestForUpdates(credentials.owner(), credentials.repo(), linkDto.lastCheck());
        return isUpdatedAfterLastCheck(response);
    }

    private boolean isUpdatedAfterLastCheck(GitHubResponse[] response) {
        return response != null && response.length > 0;
    }

}
