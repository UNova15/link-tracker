package backend.academy.linktracker.scrapper.dto.linkdto;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {

}
