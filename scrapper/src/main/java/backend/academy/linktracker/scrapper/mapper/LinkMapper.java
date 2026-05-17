package backend.academy.linktracker.scrapper.mapper;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.ListLinksResponse;
import backend.academy.linktracker.scrapper.repository.orm.entity.LinkEntity;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper {
    public List<Link> fromListLinkEntity(List<LinkEntity> linkEntities) {
        return linkEntities.stream().map(this::fromLinkEntity).toList();
    }

    public List<LinkEntity> toListOfLinkEntity(List<Link> links) {
        return links.stream().map(LinkEntity::fromDomain).toList();
    }

    public Link fromLinkEntity(LinkEntity linkEntity) {
        return Link.restore(linkEntity.getId(), linkEntity.getType(), linkEntity.getUrl(), linkEntity.getLastCheck(),linkEntity.getLastUpdate());
    }

    public ListLinksResponse toListLinkResponse(List<Subscription> subscriptions, List<Link> links) {
        Map<Long, Subscription> subscriptionsWithId =
                subscriptions.stream().collect(Collectors.toMap(Subscription::getLinkId, Function.identity()));

        List<LinkResponse> responses = links.stream()
                .map(link -> new LinkResponse(
                        link.getId(),
                        link.getUrl(),
                        List.copyOf(subscriptionsWithId.get(link.getId()).getTags())))
                .toList();

        return new ListLinksResponse(responses, responses.size());
    }

    public LinkResponse toLinkResponse(Link link, List<String> tags) {
        return new LinkResponse(link.getId(), link.getUrl(), tags);
    }
}
