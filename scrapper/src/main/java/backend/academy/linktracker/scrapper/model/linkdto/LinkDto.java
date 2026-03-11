package backend.academy.linktracker.scrapper.model.linkdto;

import backend.academy.linktracker.scrapper.model.LinkType;

import java.time.Instant;

public record LinkDto(long chatId, LinkType type, String url, String[] tags, Instant lastCheck) {

}
