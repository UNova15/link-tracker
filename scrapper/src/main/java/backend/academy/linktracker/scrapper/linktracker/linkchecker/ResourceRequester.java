package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import java.util.Optional;
import backend.academy.linktracker.scrapper.dto.linkdto.CheckResult;
import lombok.Getter;

@Getter
public abstract class ResourceRequester {
    private final LinkType linkType;

    public ResourceRequester(LinkType linkType) {
        this.linkType = linkType;
    }

    public abstract Optional<CheckResult> check(Link link);
}
