package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.linksclient.StackOverflowClient;
import backend.academy.linktracker.scrapper.model.Link;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.parser.LinkParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowChecker extends AbstractChecker {
    private StackOverflowClient client;
    private LinkParser parser;

    @Autowired
    public StackOverflowChecker(StackOverflowClient client, LinkParser parser) {
        super(LinkType.STACK_OVERFLOW);
        this.client = client;
        this.parser = parser;
    }

    @Override
    public boolean checkLink(Link link) {
        long questionId = parser.parseQuestionIdWithStackOverflowUrl(link.url());
        return client.hasActivity(questionId, link.lastCheck());
    }
}
