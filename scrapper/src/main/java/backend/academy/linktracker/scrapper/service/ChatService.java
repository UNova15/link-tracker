package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.exception.ChatAlreadyExistException;
import backend.academy.linktracker.scrapper.exception.ChatNotFoundException;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private ChatRepository repository;

    public ChatService(ChatRepository repository) {
        this.repository = repository;
    }

    public void saveChat(long chatId) {
        if (repository.exists(chatId)) {
            throw new ChatAlreadyExistException(String.format("Chat with id %d already exist", chatId));
        }

        repository.saveChat(chatId);
    }

    public void deleteChat(long chatId) {
        if (!repository.exists(chatId)) {
            throw new ChatNotFoundException(String.format("Chat with id %d does not exist", chatId));
        }
        repository.deleteChat(chatId);
    }
}
