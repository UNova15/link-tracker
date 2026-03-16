package backend.academy.linktracker.bot.model;

import java.util.List;

public record LinkResponse(long id, String url, List<String> tags) {


}
