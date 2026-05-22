package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.linkdto.CheckResult;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowContent;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowQuestion;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowResponse;
import backend.academy.linktracker.scrapper.linksclient.StackOverflowClient;
import backend.academy.linktracker.scrapper.util.LinkParser;
import backend.academy.linktracker.scrapper.util.RequesterUtil;
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
    private final RequesterUtil requesterUtil;

    public StackOverflowRequester(
            StackOverflowClient client, LinkParser parser, ResponseFormatter formatter, RequesterUtil requesterUtil) {
        super(LinkType.STACK_OVERFLOW);
        this.client = client;
        this.parser = parser;
        this.formatter = formatter;
        this.requesterUtil = requesterUtil;
    }

    @Override
    public Optional<CheckResult> check(Link link) {
        long questionId = parser.parseStackOverflowLink(link.getUrl());

        StackOverflowResponse response =
                client.sendURequestForUpdates(questionId, link.getLastUpdate().getEpochSecond());

        if (response == null || response.items() == null || response.items().isEmpty()) {
            return Optional.empty();
        }
        StackOverflowQuestion question = response.items().getFirst();

        List<StackOverflowContent> updatedComments =
                requesterUtil.filterStackOverflowContentByCreationDate(question.comments(), link.getLastUpdate());
        List<StackOverflowContent> updatedAnswers =
                requesterUtil.filterStackOverflowContentByCreationDate(question.answers(), link.getLastUpdate());

        if (updatedAnswers.isEmpty() && updatedComments.isEmpty()) {
            return Optional.empty();
        }

        String text = formatter.formatStackOverflowResponse(question.title(), updatedAnswers, updatedComments);

        Instant maxInstant = requesterUtil.findStackOverflowMaxUpdatedTime(updatedComments, updatedAnswers);

        return Optional.of(new CheckResult(text, maxInstant));
    }
}
