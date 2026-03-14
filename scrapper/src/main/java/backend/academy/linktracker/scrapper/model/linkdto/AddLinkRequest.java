package backend.academy.linktracker.scrapper.model.linkdto;

import org.hibernate.validator.constraints.URL;

public record AddLinkRequest(@URL(message = "Некорректный url") String url, String[] tags) {
}
