package backend.academy.linktracker.bot.model;

import java.util.List;

public record ListLinkResponse(List<LinkResponse> links, int size) {
}
