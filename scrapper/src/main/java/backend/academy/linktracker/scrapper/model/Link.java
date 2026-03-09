package backend.academy.linktracker.scrapper.model;

import java.time.Instant;

public record Link(long chatId, long linkId, LinkType type, String url, Instant lastCheck) {

}
