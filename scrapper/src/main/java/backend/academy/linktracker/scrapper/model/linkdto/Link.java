package backend.academy.linktracker.scrapper.model.linkdto;

import backend.academy.linktracker.scrapper.model.LinkType;

import java.time.Instant;

public record Link(long id, LinkType type, String url, String[] tags, Instant lastCheck) {

}
