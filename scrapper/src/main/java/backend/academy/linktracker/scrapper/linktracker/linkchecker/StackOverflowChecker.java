package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.StackOverflowQuestion;
import backend.academy.linktracker.scrapper.dto.StackOverflowResponse;
import backend.academy.linktracker.scrapper.linksclient.StackOverflowClient;
import backend.academy.linktracker.scrapper.util.LinkParser;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StackOverflowChecker extends ResourceRequester {
    private final StackOverflowClient client;
    private final LinkParser parser;

    public StackOverflowChecker(StackOverflowClient client, LinkParser parser) {
        super(LinkType.STACK_OVERFLOW);
        this.client = client;
        this.parser = parser;
    }

    @Override
    public Optional<String> check(Link link) {
        long questionId = parser.parseStackOverflowLink(link.getUrl());
        StackOverflowResponse<StackOverflowQuestion> response = client.sendURequestForUpdates(questionId);


    }

    private boolean isUpdatedAfterLastCheck(StackOverflowResponse<StackOverflowQuestion> response, Instant lastCheck) {
        return response != null
                && response.items() != null
                && !response.items().isEmpty()
                && Instant.ofEpochSecond(response.items().getFirst().lastActivityDate())
                        .isAfter(lastCheck);
    }
}
