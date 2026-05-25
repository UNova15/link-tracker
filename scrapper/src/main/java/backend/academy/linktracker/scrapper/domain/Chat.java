package backend.academy.linktracker.scrapper.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    private long chatId;
    private LocalDateTime createdAt;

    public static Chat createNew(long chatId) {
        if (chatId < 0) {
            throw new IllegalArgumentException("chat linkId must be positive");
        }
        return new Chat(chatId, LocalDateTime.now());
    }
}
