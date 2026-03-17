package backend.academy.linktracker.scrapper.model.linkdto;

import java.util.List;

public record LinkResponse(long id, String url, List<String> tags) {}
