package backend.academy.linktracker.scrapper.model.linkdto;

import java.util.List;

public record ListLinksResponse(LinkResponse[] links) {
    public ListLinksResponse(List<LinkDto> links) {
        LinkResponse[] response = new LinkResponse[links.size()];

        for (int i = 0; i < links.size(); i++) {
            LinkDto linkDto = links.get(i);
            response[i] = new LinkResponse(linkDto.chatId(), linkDto.url(), linkDto.tags());
        }

        this(response);
    }
}
