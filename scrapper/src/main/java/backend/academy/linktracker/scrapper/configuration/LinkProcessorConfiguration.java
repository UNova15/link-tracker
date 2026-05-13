package backend.academy.linktracker.scrapper.configuration;

import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.linktracker.LinkProcessor;
import backend.academy.linktracker.scrapper.linktracker.linkchecker.LinkChecker;
import backend.academy.linktracker.scrapper.messagesender.MessageSender;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkProcessorConfiguration {

    @Bean
    public LinkProcessor linkProcessor(
            List<LinkChecker> allCheckers, MessageSender messageSender, SubscriptionRepository subscriptionRepository) {
        Map<LinkType, LinkChecker> checkers =
                allCheckers.stream().collect(Collectors.toMap(LinkChecker::getLinkType, Function.identity()));

        return new LinkProcessor(messageSender, subscriptionRepository, checkers);
    }
}
