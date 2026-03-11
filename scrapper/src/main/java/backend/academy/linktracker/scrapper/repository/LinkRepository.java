package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.exception.LinkNotFoundException;
import backend.academy.linktracker.scrapper.model.linkdto.LinkDto;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LinkRepository {
    private final List<LinkDto> repository = new ArrayList<>();

    public List<LinkDto> findAllLinks() {
        return repository;
    }

    public void saveLink(LinkDto linkDto) {
        repository.add(linkDto);
    }

    public List<LinkDto> findLinksByChatId(long chatId) {
        return repository.stream()
            .filter(link -> link.chatId() == chatId)
            .toList();
    }

    public LinkDto removeLink(long chatId, String url) {
        LinkDto link = findLinkByUrl(chatId, url)
            .orElseThrow(() -> new LinkNotFoundException(
                String.format("Link^ %s not found", url)
            ));
        repository.remove(link);
        return link;
    }

    public Optional<LinkDto> findLinkByUrl(long chatId, String url) {
        return repository.stream()
            .filter(l -> l.chatId() == chatId && l.url().equals(url))
            .findAny();
    }

    public boolean exists(long chatId, String url) {
        return findLinkByUrl(chatId,url).isPresent();
    }
}
