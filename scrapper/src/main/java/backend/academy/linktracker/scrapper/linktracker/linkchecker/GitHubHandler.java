package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.github.IssueCredential;
import backend.academy.linktracker.scrapper.dto.linkdto.ProcessingResult;
import backend.academy.linktracker.scrapper.linksclient.GitHubService;
import backend.academy.linktracker.scrapper.util.LinkParser;
import backend.academy.linktracker.scrapper.util.RequesterUtil;
import backend.academy.linktracker.scrapper.util.ResponseFormatter;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GitHubHandler extends ResourceHandler {
    private final GitHubService gitHub;
    private final ResponseFormatter formatter;
    private final LinkParser parser;
    private final RequesterUtil requesterUtil;

    public GitHubHandler(
            GitHubService gitHub, LinkParser parser, ResponseFormatter formatter, RequesterUtil requesterUtil) {
        super(LinkType.GIT_HUB);
        this.parser = parser;
        this.gitHub = gitHub;
        this.formatter = formatter;
        this.requesterUtil = requesterUtil;
    }

    @Override
    public Optional<ProcessingResult> process(Link link) {
        IssueCredential credentials = parser.parseGitHubLink(link.getUrl());

        List<GitHubResponse> response =
                gitHub.sendURequestForUpdates(credentials.owner(), credentials.repo(), link.getLastUpdate());

        if (response == null || response.isEmpty()) {
            return Optional.empty();
        }

        // фильтрация по новым обновлениям
        List<GitHubResponse> filteredChanges =
                requesterUtil.filterGitContentByCreationDate(response, link.getLastUpdate());

        if (filteredChanges.isEmpty()) {
            return Optional.empty();
        }

        String formattedResponse = formatter.formatGitHubResponse(filteredChanges);
        Instant maxUpdateTime = requesterUtil.findGitHubMaxUpdatedTime(filteredChanges);

        return Optional.of(new ProcessingResult(formattedResponse, maxUpdateTime));
    }
}
