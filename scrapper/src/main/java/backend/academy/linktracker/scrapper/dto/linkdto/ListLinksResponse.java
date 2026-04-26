package backend.academy.linktracker.scrapper.dto.linkdto;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.Subscription;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ListLinksResponse(List<LinkResponse> links, int size) {
    public ListLinksResponse(List<Link> links, List<Subscription> subscriptions) {
        Map<Long, Subscription> subMap =
                subscriptions.stream().collect(Collectors.toMap(Subscription::linkId, Function.identity()));

        List<LinkResponse> responses = links.stream()
                .map(link -> new LinkResponse(
                        link.id(), link.url(), subMap.get(link.id()).tags()))
                .toList();

        this(responses, responses.size());
    }
}
