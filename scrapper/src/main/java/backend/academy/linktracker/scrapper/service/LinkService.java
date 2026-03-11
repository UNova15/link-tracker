package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.exception.ChatNotFoundException;
import backend.academy.linktracker.scrapper.exception.LinkAlreadyExistException;
import backend.academy.linktracker.scrapper.exception.LinkNotFoundException;
import backend.academy.linktracker.scrapper.model.linkdto.AddLinkRequest;
import backend.academy.linktracker.scrapper.model.linkdto.LinkDto;
import backend.academy.linktracker.scrapper.model.linkdto.LinkResponse;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.model.linkdto.ListLinksResponse;
import backend.academy.linktracker.scrapper.model.linkdto.RemoveLinkRequest;
import backend.academy.linktracker.scrapper.parser.LinkParser;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.List;

@Component
public class LinkService {
    private final LinkParser parser;
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository, ChatRepository chatRepository, LinkParser parser) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
        this.parser = parser;
    }

    public ListLinksResponse findLinkByChatId(long chatId) {
        if (!chatRepository.exists(chatId)) {
            throw new ChatNotFoundException(String.format("Chat with url: %s not exist", chatId));
        }

        List<LinkDto> links = linkRepository.findLinksByChatId(chatId);
        return new ListLinksResponse(links);
    }

    public LinkResponse saveLink(long chatId, AddLinkRequest request) {
        if (!chatRepository.exists(chatId)) {
            throw new ChatNotFoundException(String.format("Chat with url: %s not exist", chatId));
        }

        if (linkRepository.exists(chatId, request.url())) {
            throw new LinkAlreadyExistException(String.format(
                "Link url: %s with user: %d already exist", request.url(), chatId)
            );
        }
        LinkType type = parser.parseLinkType(request.url());
        LinkDto linkDto = new LinkDto(chatId, type, request.url(), request.tags(), Instant.now());
        linkRepository.saveLink(linkDto);

        return new LinkResponse(linkDto.chatId(), linkDto.url(), linkDto.tags());
    }

    public LinkResponse removeLink(long chatId, RemoveLinkRequest request) {
        if (chatRepository.exists(chatId)) {
            throw new ChatNotFoundException(String.format("Chat with url: %s not exist", chatId));
        }

        if (!linkRepository.exists(chatId, request.link())) {
            throw new LinkNotFoundException(String.format("Link with url: %s not found", request.link()));
        }

        LinkDto link = linkRepository.removeLink(chatId, request.link());
        return new LinkResponse(link.chatId(), link.url(), link.tags());
    }
}
