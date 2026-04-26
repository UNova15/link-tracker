package backend.academy.linktracker.scrapper.domain;

import java.time.Instant;

public record Chat(long chatId, Instant createdAt) {}
