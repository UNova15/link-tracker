package backend.academy.linktracker.ai.dto;

import backend.academy.linktracker.ai.domain.Notification;
import java.util.List;

public record AggregatedNotification(List<Notification> notifications) {}
