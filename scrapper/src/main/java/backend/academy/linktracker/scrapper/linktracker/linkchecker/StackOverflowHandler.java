package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.linkdto.ProcessingResult;
import backend.academy.linktracker.scrapper.dto.linkdto.Update;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowContent;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowQuestion;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowResponse;
import backend.academy.linktracker.scrapper.linksclient.StackOverflowService;
import backend.academy.linktracker.scrapper.mapper.NotificationMapper;
import backend.academy.linktracker.scrapper.util.LinkParser;
import backend.academy.linktracker.scrapper.util.UpdateHandlerUtil;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StackOverflowHandler extends UpdateHandler {
    private final StackOverflowService stackOverflow;
    private final NotificationMapper mapper;
    private final LinkParser parser;
    private final UpdateHandlerUtil updateHandlerUtil;

    public StackOverflowHandler(
            StackOverflowService stackOverflow,
            NotificationMapper mapper,
            LinkParser parser,
            UpdateHandlerUtil updateHandlerUtil) {
        super(LinkType.STACK_OVERFLOW);
        this.stackOverflow = stackOverflow;
        this.mapper = mapper;
        this.parser = parser;
        this.updateHandlerUtil = updateHandlerUtil;
    }

    @Override
    public Optional<ProcessingResult> process(Link link) {
        long questionId = parser.parseStackOverflowLink(link.getUrl());

        StackOverflowResponse response = stackOverflow.sendURequestForUpdates(
                questionId, link.getLastUpdate().getEpochSecond());

        if (response == null || response.items() == null || response.items().isEmpty()) {
            return Optional.empty();
        }
        StackOverflowQuestion question = response.items().getFirst();

        List<StackOverflowContent> content = updateHandlerUtil.filterStackOverflowContentByCreationDate(
                question.comments(), question.answers(), link.getLastUpdate());

        if (content.isEmpty()) {
            return Optional.empty();
        }
        Instant maxInstant = updateHandlerUtil.findStackOverflowMaxUpdateTime(content);

        List<Update> updates = mapper.fromStackOverflowContentToUpdate(content);

        return Optional.of(new ProcessingResult(updates, maxInstant));
    }
}
