package backend.academy.linktracker.scrapper.domain;

import java.util.List;

public record Subscription(long chatId, long linkId, List<String> tags) {}
