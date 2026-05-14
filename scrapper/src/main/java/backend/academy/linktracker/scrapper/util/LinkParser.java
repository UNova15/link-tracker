package backend.academy.linktracker.scrapper.util;

import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.github.IssueCredential;
import backend.academy.linktracker.scrapper.exception.IncorrectLinkFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class LinkParser {
    private final Pattern GIT_HUB_PATTERN = Pattern.compile("github\\.com/([^/]+)/([^/#&]+)");
    private final Pattern STACK_OVERFLOW_PATTERN = Pattern.compile("stackoverflow\\.com/questions/(\\d+)");

    public LinkType parseLinkType(String url) {
        if (GIT_HUB_PATTERN.matcher(url).find()) {
            return LinkType.GIT_HUB;
        } else if (STACK_OVERFLOW_PATTERN.matcher(url).find()) {
            return LinkType.STACK_OVERFLOW;
        } else {
            throw new IncorrectLinkFormatException("Incorrect link type: %s".formatted(url));
        }
    }

    public IssueCredential parseGitHubLink(String url) {
        Matcher matcher = GIT_HUB_PATTERN.matcher(url);
        if (matcher.find()) {
            return new IssueCredential(matcher.group(1), matcher.group(2));
        }
        throw new IncorrectLinkFormatException("Incorrect GitHub link: %s".formatted(url));
    }

    public long parseStackOverflowLink(String url) {
        Matcher matcher = STACK_OVERFLOW_PATTERN.matcher(url);

        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new IncorrectLinkFormatException("Incorrect StackOverflow link: %s".formatted(url));
    }
}
