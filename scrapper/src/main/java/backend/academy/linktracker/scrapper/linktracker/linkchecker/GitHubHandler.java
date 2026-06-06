package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.github.IssueCredential;
import backend.academy.linktracker.scrapper.dto.linkdto.ProcessingResult;
import backend.academy.linktracker.scrapper.dto.linkdto.Update;
import backend.academy.linktracker.scrapper.linksclient.GitHubService;
import backend.academy.linktracker.scrapper.mapper.NotificationMapper;
import backend.academy.linktracker.scrapper.util.LinkParser;
import backend.academy.linktracker.scrapper.util.UpdateHandlerUtil;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GitHubHandler extends UpdateHandler {
    private final GitHubService gitHub;
    private final NotificationMapper mapper;
    private final LinkParser parser;
    private final UpdateHandlerUtil requestUtil;

    public GitHubHandler(
            GitHubService gitHub, LinkParser parser, UpdateHandlerUtil requestUtil, NotificationMapper mapper) {
        super(LinkType.GIT_HUB);
        this.parser = parser;
        this.gitHub = gitHub;
        this.mapper = mapper;
        this.requestUtil = requestUtil;
    }

    @Override
    public Optional<ProcessingResult> process(Link link) {
        IssueCredential credentials = parser.parseGitHubLink(link.getUrl());

        List<GitHubResponse> response =
                gitHub.sendURequestForUpdates(credentials.owner(), credentials.repo(), link.getLastUpdate());
        if (response == null || response.isEmpty()) {
            return Optional.empty();
        }

        List<GitHubResponse> filteredChanges =
                requestUtil.filterGitHubContentByCreationDate(response, link.getLastUpdate());

        if (filteredChanges.isEmpty()) {
            return Optional.empty();
        }
        Instant maxUpdateTime = requestUtil.findGitHubMaxUpdateTime(filteredChanges);
        List<Update> updates = mapper.fromGitHubContentToUpdate(filteredChanges);

        return Optional.of(new ProcessingResult(updates, maxUpdateTime));
    }
}
