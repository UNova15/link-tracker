package backend.academy.linktracker.scrapper.dto.linkdto;

import java.util.List;
import org.hibernate.validator.constraints.URL;

public record AddLinkRequest(
        @URL(message = "Некорректный url") String url, List<String> tags) {}
