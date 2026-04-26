package backend.academy.linktracker.bot.dto;

import java.util.List;
import org.hibernate.validator.constraints.URL;

public record AddLinkRequest(
        @URL(message = "Некорректный url") String url, List<String> tags) {}
