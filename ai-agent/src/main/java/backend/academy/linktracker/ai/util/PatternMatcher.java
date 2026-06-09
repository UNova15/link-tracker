package backend.academy.linktracker.ai.util;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PatternMatcher {
    private static final String PATTERN = "\\b(%s)\\b";

    public Pattern patternForSearchingWords(List<String> searchingWords) {
        String combinedWords = String.join("|", searchingWords);
        String createdPattern = PATTERN.formatted(combinedWords);
        return Pattern.compile(createdPattern, Pattern.CASE_INSENSITIVE);
    }
}
