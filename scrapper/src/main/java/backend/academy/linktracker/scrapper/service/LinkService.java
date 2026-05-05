package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.parser.LinkParser;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;
    private final LinkParser parser;

    public Link saveLink(String url) {
        LinkType type = parser.parseLinkType(url);
        Link link = Link.createNew(type, url);
        return linkRepository.save(link);
    }

    public Link findOrCreateLink(String url) {
        return linkRepository.findByUrl(url)
            .orElseGet(() -> saveLink(url));
    }

    public List<Link> findLinksByIds(List<Long> linkIds){
        return linkRepository.findAllByIdIn(linkIds);
    }

    public Optional<Link> findByUrl(String url){
        return linkRepository.findByUrl(url);
    }

    public void deleteLink(String url){
        linkRepository.removeByUrl(url);
    }
}
