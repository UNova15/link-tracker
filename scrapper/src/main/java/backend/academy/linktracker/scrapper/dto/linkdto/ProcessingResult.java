package backend.academy.linktracker.scrapper.dto.linkdto;

import java.time.Instant;
import java.util.List;

public record ProcessingResult(List<Update> updates, Instant newUpdateTime) {}
