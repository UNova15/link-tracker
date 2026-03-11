package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.model.linkdto.LinkDto;
import backend.academy.linktracker.scrapper.model.LinkType;
import lombok.Getter;

@Getter
public abstract class LinkChecker {
    private final LinkType linkType;

    public LinkChecker(LinkType linkType){
        this.linkType = linkType;
    }

    public abstract boolean checkLink(LinkDto linkDto);
}
