package backend.academy.linktracker.scrapper.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    private long chatId;
    private LocalDateTime createdAt;

    public static Chat createNew(long chatId) {
        if (chatId < 0) {
            throw new IllegalArgumentException("chat id must be positive");
        }
        return new Chat(chatId, LocalDateTime.now());
    }
}
