package backend.academy.linktracker.scrapper.model.linkdto;

import backend.academy.linktracker.scrapper.model.LinkType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.time.Instant;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Link {
    private final long id;
    private final LinkType type;
    private final String url;
    private final String[] tags;

    @Setter
    @NonNull
    private Instant lastCheck;
}
