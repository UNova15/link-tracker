package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.linksclient.GitHubClient;
import backend.academy.linktracker.scrapper.model.GitHubRepositoryInfo;
import backend.academy.linktracker.scrapper.model.Link;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.parser.LinkParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GitHubLinkChecker extends AbstractChecker {
    private GitHubClient client;
    private LinkParser parser;

    @Autowired
    public GitHubLinkChecker(GitHubClient client, LinkParser parser) {
        super(LinkType.GIT_HUB);
        this.client = client;
        this.parser = parser;
    }

    @Override
    public boolean checkLink(Link link) {
        GitHubRepositoryInfo credentials = parser.parseCredentialWithGitHubUrl(link.url());
        return client.hasActivity(credentials.owner(), credentials.repo(), link.lastCheck());
    }
}
