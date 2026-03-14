package backend.academy.linktracker.scrapper.controller;


import backend.academy.linktracker.scrapper.model.linkdto.AddLinkRequest;
import backend.academy.linktracker.scrapper.model.linkdto.LinkResponse;
import backend.academy.linktracker.scrapper.model.linkdto.ListLinksResponse;
import backend.academy.linktracker.scrapper.model.linkdto.RemoveLinkRequest;
import backend.academy.linktracker.scrapper.service.LinkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinksController {
    private final LinkService service;

    @Autowired
    public LinksController(LinkService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long chatId) {
        ListLinksResponse response = service.findLinksByChatId(chatId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") long chatId, @RequestBody @Valid AddLinkRequest request) {
        LinkResponse response = service.saveLink(chatId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> deleteLink(@RequestHeader("Tg-Chat-Id") long chatId, @RequestBody @Valid RemoveLinkRequest request) {
        LinkResponse response = service.removeLink(chatId, request);
        return ResponseEntity.ok().body(response);
    }

}
