package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.domain.Notification;

public interface MessageSender {

    void send(Notification update);
}
