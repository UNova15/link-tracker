package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;

public interface MessageSender {

    void send(LinkUpdate update);
}
