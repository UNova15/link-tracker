package backend.academy.linktracker.ai.configuration;

import backend.academy.linktracker.ai.properties.FilterProperties;
import backend.academy.linktracker.ai.properties.PrioritizationProperties;
import backend.academy.linktracker.ai.service.MessageFilter;
import backend.academy.linktracker.ai.service.PrioritizeService;
import backend.academy.linktracker.ai.util.PatternMatcher;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessingServiceConfiguration {

    @Bean
    public MessageFilter messageFilter(FilterProperties properties, PatternMatcher matcher) {
        Set<String> excludedAuthors = new HashSet<>(properties.excludedAuthors());
        return new MessageFilter(
                properties.minLength(), excludedAuthors, matcher.patternForSearchingWords(properties.stopWords()));
    }

    @Bean
    public PrioritizeService prioritizeService(PrioritizationProperties properties, PatternMatcher matcher) {
        return new PrioritizeService(
                matcher.patternForSearchingWords(properties.highKeywords()),
                matcher.patternForSearchingWords(properties.lowKeywords()));
    }
}
