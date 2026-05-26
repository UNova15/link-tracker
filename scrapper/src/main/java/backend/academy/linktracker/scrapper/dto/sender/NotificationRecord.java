package backend.academy.linktracker.scrapper.dto.sender;

import backend.academy.linktracker.scrapper.domain.Notification;

public record NotificationRecord(long id, Notification notification) {}
