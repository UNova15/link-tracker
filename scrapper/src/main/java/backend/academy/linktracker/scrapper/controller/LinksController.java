package backend.academy.linktracker.scrapper.controller;

import backend.academy.linktracker.scrapper.dto.linkdto.AddLinkRequest;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.ListLinksResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.RemoveLinkRequest;
import backend.academy.linktracker.scrapper.service.subscriptionservice.SubscriptionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class LinksController {
    private final SubscriptionService service;

    @GetMapping
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long chatId) {
        ListLinksResponse response = service.findSubscriptionsWithLinks(chatId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
            @RequestHeader("Tg-Chat-Id") long chatId, @RequestBody @Valid AddLinkRequest request) {
        LinkResponse response = service.createSubscription(chatId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> deleteLink(
            @RequestHeader("Tg-Chat-Id") long chatId, @RequestBody @Valid RemoveLinkRequest request) {
        LinkResponse response = service.removeSubscription(chatId, request);
        return ResponseEntity.ok().body(response);
    }
}
