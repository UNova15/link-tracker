package backend.academy.linktracker.scrapper.linktracker.linkchecker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.linkdto.ProcessingResult;
import java.util.Optional;
import lombok.Getter;

@Getter
public abstract class UpdateHandler {
    private final LinkType linkType;

    public UpdateHandler(LinkType linkType) {
        this.linkType = linkType;
    }

    public abstract Optional<ProcessingResult> process(Link link);
}
