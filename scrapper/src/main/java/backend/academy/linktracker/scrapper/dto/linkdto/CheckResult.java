package backend.academy.linktracker.scrapper.dto.linkdto;

import java.time.Instant;

public record CheckResult(String text, Instant newUpdateTime) {
}
