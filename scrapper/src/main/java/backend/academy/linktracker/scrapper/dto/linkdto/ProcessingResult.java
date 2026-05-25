package backend.academy.linktracker.scrapper.dto.linkdto;

import java.time.Instant;

public record ProcessingResult(String text, Instant newUpdateTime) {}
