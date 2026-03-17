package backend.academy.linktracker.scrapper.model.linkdto;

import org.hibernate.validator.constraints.URL;
import java.util.List;

public record AddLinkRequest(@URL(message = "Некорректный url") String url, List<String> tags) {
}
