package backend.academy.linktracker.bot.controller;

import backend.academy.linktracker.bot.model.LinkUpdate;
import backend.academy.linktracker.bot.service.UpdateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class UpdateController {
    private final UpdateService updateService;

    @Autowired
    public UpdateController(UpdateService updateService) {
        this.updateService = updateService;
    }

    @PostMapping
    public ResponseEntity<Void> sendUpdate(@RequestBody @Valid LinkUpdate linkUpdate) {
        updateService.sendUpdateMessage(linkUpdate);
        return ResponseEntity.ok().build();
    }
}
