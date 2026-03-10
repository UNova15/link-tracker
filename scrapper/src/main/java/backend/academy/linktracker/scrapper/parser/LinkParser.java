package backend.academy.linktracker.scrapper.parser;

import backend.academy.linktracker.scrapper.model.GitHubRepositoryInfo;
import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LinkParser {
    private final Pattern GIT_HUB_PATTERN = Pattern.compile("github\\.com/([^/]+)/([^/#&]+)");
    private final Pattern STACK_OVERFLOW_PATTERN = Pattern.compile("stackoverflow\\.com/questions/(\\d+)");

    public GitHubRepositoryInfo parseGitHubUrl(String url) {
        Matcher matcher = GIT_HUB_PATTERN.matcher(url);

        if (matcher.find()) {
            return new GitHubRepositoryInfo(matcher.group(1), matcher.group(2));
        }
        throw new IllegalArgumentException();
    }

    public long parseStackOverflowUrl(String url) {
        Matcher matcher = STACK_OVERFLOW_PATTERN.matcher(url);

        if(matcher.find()){
            return Long.parseLong(matcher.group(1));
        }
        throw new IllegalArgumentException();
    }
}
