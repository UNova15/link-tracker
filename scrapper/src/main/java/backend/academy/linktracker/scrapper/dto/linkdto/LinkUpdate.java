package backend.academy.linktracker.scrapper.dto.linkdto;

import java.util.List;

public record LinkUpdate(long id, String url, String description, List<Long> tgChatIds) {}
