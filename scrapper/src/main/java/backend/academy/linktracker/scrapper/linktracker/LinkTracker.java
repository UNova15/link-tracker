package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.linktracker.linkchecker.LinkChecker;
import backend.academy.linktracker.scrapper.model.linkdto.LinkDto;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LinkTracker {
    private LinkRepository repository;
    private Map<LinkType, LinkChecker> checkers;

    @Autowired
    public LinkTracker(List<LinkChecker> checkers, LinkRepository repository) {
        this.repository = repository;
        this.checkers = checkers.stream()
            .collect(Collectors.toMap(LinkChecker::getLinkType, Function.identity()));
    }

    @Scheduled(fixedDelayString = "${app.shedulerinterval}")
    public void updateNotification() {
        List<LinkDto> activeLinkDto = repository.findAllLinks();

        for (LinkDto linkDto : activeLinkDto) {
            LinkChecker checker = checkers.get(linkDto.type());
            boolean isUpdated = checker.checkLink(linkDto);

            if (isUpdated) {
                //отправка уведомления /update
            }
        }
    }
}
