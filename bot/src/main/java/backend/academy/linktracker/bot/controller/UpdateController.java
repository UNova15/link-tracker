package backend.academy.linktracker.bot.controller;

import backend.academy.linktracker.bot.dto.LinkUpdate;
import backend.academy.linktracker.bot.service.UpdateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@AllArgsConstructor
public class UpdateController {
    private final UpdateService updateService;

    @PostMapping
    public ResponseEntity<Void> sendUpdate(@RequestBody @Valid LinkUpdate linkUpdate) {
        updateService.sendUpdateMessage(linkUpdate);
        return ResponseEntity.ok().build();
    }
}
