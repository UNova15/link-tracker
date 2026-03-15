package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.linksclient.StackOverflowClient;
import backend.academy.linktracker.scrapper.model.linkdto.Link;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.model.StackOverflowQuestion;
import backend.academy.linktracker.scrapper.model.StackOverflowResponse;
import backend.academy.linktracker.scrapper.parser.LinkParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class StackOverflowChecker extends LinkChecker {
    private final StackOverflowClient client;
    private final LinkParser parser;

    @Autowired
    public StackOverflowChecker(StackOverflowClient client, LinkParser parser) {
        super(LinkType.STACK_OVERFLOW);
        this.client = client;
        this.parser = parser;
    }

    @Override
    public boolean checkLink(Link link) {
        long questionId = parser.parseStackOverflowLink(link.getUrl());
        StackOverflowResponse<StackOverflowQuestion> response = client.sendURequestForUpdates(questionId);
        return isUpdatedAfterLastCheck(response, link.getLastCheck());
    }

    private boolean isUpdatedAfterLastCheck(StackOverflowResponse<StackOverflowQuestion> response, Instant lastCheck) {
        return response != null
            && response.items() != null
            && !response.items().isEmpty()
            && Instant.ofEpochSecond(response.items().getFirst().lastActivityDate()).isAfter(lastCheck);
    }
}
