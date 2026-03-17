package backend.academy.linktracker.scrapper.model;

import java.util.List;

public record Subscription(long chatId, long linkId, List<String> tags) {
}
