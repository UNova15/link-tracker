package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.model.LinkType;
import lombok.Getter;

@Getter
public abstract class AbstractChecker implements LinkChecker {
    private final LinkType linkType;

    public AbstractChecker(LinkType linkType){
        this.linkType = linkType;
    }

}
