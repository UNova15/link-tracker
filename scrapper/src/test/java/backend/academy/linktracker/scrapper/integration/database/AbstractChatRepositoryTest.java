package backend.academy.linktracker.scrapper.integration.database;

import backend.academy.linktracker.scrapper.domain.Chat;
import backend.academy.linktracker.scrapper.integration.TestcontainersConfiguration;
import backend.academy.linktracker.scrapper.linktracker.LinkTracker;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest("spring.main.lazy-initialization=true")
@Transactional
@Import(TestcontainersConfiguration.class)
public abstract class AbstractChatRepositoryTest {

    @Autowired
    protected ChatRepository chatRepository;

    @MockitoBean
    protected LinkTracker linkTracker;

    @Test
    void save_withValidChat_saveLink(){
        Chat chat = Chat.createNew(1);

        chatRepository.save(chat);

        assertThat(chatRepository.existById(chat.getChatId())).isTrue();
    }

    @Test
    void deleteById_withValidChat_deleteChat(){
        Chat chat = Chat.createNew(1);

        chatRepository.deleteById(chat.getChatId());

        assertThat(chatRepository.existById(chat.getChatId())).isFalse();
    }
}
