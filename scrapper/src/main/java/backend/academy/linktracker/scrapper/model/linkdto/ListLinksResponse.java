package backend.academy.linktracker.scrapper.model.linkdto;

import java.util.ArrayList;
import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {
    public ListLinksResponse(long chatId, List<Link> links) {
        List<LinkResponse> response = new ArrayList<>(links.size());

        for (Link link : links) {
            response.add(new LinkResponse(chatId, link.url(), link.tags()));
        }

        this(response, response.size());
    }
}
