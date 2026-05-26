package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.github.IssueCredential;
import backend.academy.linktracker.scrapper.dto.linkdto.ProcessingResult;
import backend.academy.linktracker.scrapper.linksclient.GitHubClient;
import backend.academy.linktracker.scrapper.util.LinkParser;
import backend.academy.linktracker.scrapper.util.RequesterUtil;
import backend.academy.linktracker.scrapper.util.ResponseFormatter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class GitHubHandler extends ResourceHandler {
    private final GitHubClient client;
    private final ResponseFormatter formatter;
    private final LinkParser parser;
    private final RequesterUtil requesterUtil;

    public GitHubHandler(
            GitHubClient client, LinkParser parser, ResponseFormatter formatter, RequesterUtil requesterUtil) {
        super(LinkType.GIT_HUB);
        this.client = client;
        this.parser = parser;
        this.formatter = formatter;
        this.requesterUtil = requesterUtil;
    }

    @Override
    public Optional<ProcessingResult> process(Link link) {
        IssueCredential credentials = parser.parseGitHubLink(link.getUrl());

        String sinceTime = link.getLastUpdate().truncatedTo(ChronoUnit.SECONDS).toString();
        List<GitHubResponse> response =
                client.sendURequestForUpdates(credentials.owner(), credentials.repo(), sinceTime);

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
