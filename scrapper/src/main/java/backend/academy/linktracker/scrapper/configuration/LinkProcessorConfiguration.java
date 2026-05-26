package backend.academy.linktracker.scrapper.configuration;

import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.linktracker.LinkProcessor;
import backend.academy.linktracker.scrapper.linktracker.linkchecker.ResourceHandler;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkProcessorConfiguration {
    @Value("${app.number-of-threads}")
    private int numberOfThreads;

    @Bean
    public LinkProcessor linkProcessor(
            List<ResourceHandler> allCheckers,
            SubscriptionRepository subscriptionRepository,
            ExecutorService executorService) {
        Map<LinkType, ResourceHandler> checkers =
                allCheckers.stream().collect(Collectors.toMap(ResourceHandler::getLinkType, Function.identity()));

        return new LinkProcessor(subscriptionRepository, checkers, executorService, numberOfThreads);
    }

    @Bean
    public ExecutorService linkProcessorThreadPool() {
        return Executors.newFixedThreadPool(numberOfThreads);
    }
}
