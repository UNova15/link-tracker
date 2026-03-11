package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.linksclient.StackOverflowClient;
import backend.academy.linktracker.scrapper.model.linkdto.LinkDto;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.model.StackOverflowQuestion;
import backend.academy.linktracker.scrapper.model.StackOverflowResponse;
import backend.academy.linktracker.scrapper.parser.LinkParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class StackOverflowChecker extends LinkChecker {
    private StackOverflowClient client;
    private LinkParser parser;

    @Autowired
    public StackOverflowChecker(StackOverflowClient client, LinkParser parser) {
        super(LinkType.STACK_OVERFLOW);
        this.client = client;
        this.parser = parser;
    }

    @Override
    public boolean checkLink(LinkDto linkDto) {
        long questionId = parser.parseStackOverflowLink(linkDto.url());
        StackOverflowResponse<StackOverflowQuestion> response = client.sendURequestForUpdates(questionId);
        return isUpdatedAfterLastCheck(response, linkDto.lastCheck());
    }

    private boolean isUpdatedAfterLastCheck(StackOverflowResponse<StackOverflowQuestion> response, Instant lastCheck) {
        return response != null
            && response.items() != null
            && !response.items().isEmpty()
            && Instant.ofEpochSecond(response.items().getFirst().lastActivityDate()).isAfter(lastCheck);
    }
}
