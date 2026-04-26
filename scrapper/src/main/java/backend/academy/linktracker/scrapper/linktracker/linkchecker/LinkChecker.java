package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Link;
import lombok.Getter;

@Getter
public abstract class LinkChecker {
    private final LinkType linkType;

    public LinkChecker(LinkType linkType) {
        this.linkType = linkType;
    }

    public abstract boolean checkLink(Link link);
}
