package backend.academy.linktracker.scrapper.configuration;

import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.linktracker.LinkProcessor;
import backend.academy.linktracker.scrapper.linktracker.linkchecker.UpdateHandler;
import backend.academy.linktracker.scrapper.properties.ScrapperProperties;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkProcessorConfiguration {
    @Bean
    public LinkProcessor linkProcessor(
            List<UpdateHandler> allCheckers,
            SubscriptionRepository subscriptionRepository,
            ExecutorService executorService,
            ScrapperProperties properties) {
        Map<LinkType, UpdateHandler> checkers =
                allCheckers.stream().collect(Collectors.toMap(UpdateHandler::getLinkType, Function.identity()));

        return new LinkProcessor(subscriptionRepository, checkers, executorService, properties.numberOfThreads());
    }

    @Bean
    public ExecutorService linkProcessorThreadPool(ScrapperProperties properties) {
        return Executors.newFixedThreadPool(properties.numberOfThreads());
    }
}
