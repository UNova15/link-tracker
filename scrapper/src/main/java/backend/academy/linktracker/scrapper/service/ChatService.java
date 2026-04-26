package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.exception.ChatAlreadyExistException;
import backend.academy.linktracker.scrapper.exception.ChatNotFoundException;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatRepository repository;

    public void saveChat(long chatId) {
        if (repository.exists(chatId)) {
            throw new ChatAlreadyExistException(chatId);
        }
        repository.saveChat(chatId);
    }

    public void deleteChat(long chatId) {
        if (!repository.exists(chatId)) {
            throw new ChatNotFoundException(chatId);
        }
        repository.deleteChat(chatId);
    }
}
