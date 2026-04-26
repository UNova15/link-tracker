package backend.academy.linktracker.scrapper.parser;

import backend.academy.linktracker.scrapper.exception.IncorrectLinkFormatException;
import backend.academy.linktracker.scrapper.dto.GitHubDto;
import backend.academy.linktracker.scrapper.domain.LinkType;
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
            throw new IncorrectLinkFormatException(String.format("Incorrect link type: %s", url));
        }
    }

    public GitHubDto parseGitHubLink(String url) {
        Matcher matcher = GIT_HUB_PATTERN.matcher(url);
        if (matcher.find()) {
            return new GitHubDto(matcher.group(1), matcher.group(2));
        }
        throw new IncorrectLinkFormatException(String.format("Incorrect GitHub link: %s", url));
    }

    public long parseStackOverflowLink(String url) {
        Matcher matcher = STACK_OVERFLOW_PATTERN.matcher(url);

        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new IncorrectLinkFormatException(String.format("Incorrect StackOverflow link: %s", url));
    }
}
