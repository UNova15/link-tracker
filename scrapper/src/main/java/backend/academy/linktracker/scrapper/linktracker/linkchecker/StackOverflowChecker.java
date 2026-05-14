package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
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
public class StackOverflowChecker extends ResourceRequester {
    private final StackOverflowClient client;
    private final ResponseFormatter formatter;
    private final LinkParser parser;

    public StackOverflowChecker(StackOverflowClient client, LinkParser parser, ResponseFormatter formatter) {
        super(LinkType.STACK_OVERFLOW);
        this.client = client;
        this.parser = parser;
        this.formatter = formatter;
    }

    @Override
    public Optional<String> check(Link link) {
        long questionId = parser.parseStackOverflowLink(link.getUrl());
        StackOverflowResponse response = client.sendURequestForUpdates(questionId, link.getLastCheck());

        if (response == null || response.items() == null || response.items().isEmpty()) {
            return Optional.empty();
        }
        StackOverflowQuestion question = response.items().getFirst();

        List<StackOverflowContent> updatedComments =
                filterContentByCreationDate(question.comments(), link.getLastCheck());
        List<StackOverflowContent> updatedAnswers =
                filterContentByCreationDate(question.answers(), link.getLastCheck());

        if (updatedAnswers.isEmpty() && updatedComments.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(formatter.formatStackOverflowResponse(question.title(), updatedAnswers, updatedComments));
    }

    private List<StackOverflowContent> filterContentByCreationDate(
            List<StackOverflowContent> content, Instant lastCheck) {
        return content.stream()
                .filter(comment -> Instant.ofEpochSecond(comment.creationDate()).isAfter(lastCheck))
                .toList();
    }
}
