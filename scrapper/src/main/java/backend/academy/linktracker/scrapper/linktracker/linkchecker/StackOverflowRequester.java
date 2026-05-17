package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.CheckResult;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowContent;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowQuestion;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowResponse;
import backend.academy.linktracker.scrapper.linksclient.StackOverflowClient;
import backend.academy.linktracker.scrapper.util.LinkParser;
import backend.academy.linktracker.scrapper.util.ResponseFormatter;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StackOverflowRequester extends ResourceRequester {
    private final StackOverflowClient client;
    private final ResponseFormatter formatter;
    private final LinkParser parser;

    public StackOverflowRequester(StackOverflowClient client, LinkParser parser, ResponseFormatter formatter) {
        super(LinkType.STACK_OVERFLOW);
        this.client = client;
        this.parser = parser;
        this.formatter = formatter;
    }

    @Override
    public Optional<CheckResult> check(Link link) {
        long questionId = parser.parseStackOverflowLink(link.getUrl());
        Instant since = link.getLastUpdate() != null ? link.getLastUpdate() : link.getLastCheck();

        StackOverflowResponse response = client.sendURequestForUpdates(questionId, since);


        if (response == null || response.items() == null || response.items().isEmpty()) {
            return Optional.empty();
        }
        StackOverflowQuestion question = response.items().getFirst();

        List<StackOverflowContent> updatedComments =
            filterContentByCreationDate(question.comments(), since);
        List<StackOverflowContent> updatedAnswers =
            filterContentByCreationDate(question.answers(), since);

        if (updatedAnswers.isEmpty() && updatedComments.isEmpty()) {
            return Optional.empty();
        }

        String text = formatter.formatStackOverflowResponse(question.title(), updatedAnswers, updatedComments);

        long maxCommentUpdateTime = updatedComments.stream().mapToLong(StackOverflowContent::creationDate).max().orElse(0);
        long maxAnswerUpdateTime = updatedAnswers.stream().mapToLong(StackOverflowContent::creationDate).max().orElse(0);
        long max = Math.max(maxAnswerUpdateTime, maxCommentUpdateTime);

        Instant maxInstant = max != 0 ? Instant.ofEpochSecond(max) : null;

        return Optional.of(new CheckResult(text, maxInstant));
    }

    private List<StackOverflowContent> filterContentByCreationDate(
        List<StackOverflowContent> content, Instant lastCheck) {
        if (content == null) {
            return List.of();
        }

        return content.stream()
            .filter(comment -> Instant.ofEpochSecond(comment.creationDate()).isAfter(lastCheck))
            .toList();
    }
}
