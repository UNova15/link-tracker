package backend.academy.linktracker.scrapper.model;

import java.time.Instant;

public record Chat(long chatId, Instant createdAt) {}
