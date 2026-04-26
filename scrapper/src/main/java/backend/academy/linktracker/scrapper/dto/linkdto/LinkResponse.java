package backend.academy.linktracker.scrapper.dto.linkdto;

import java.util.List;

public record LinkResponse(long id, String url, List<String> tags) {}
