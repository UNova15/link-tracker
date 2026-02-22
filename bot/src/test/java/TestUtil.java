import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.utility.BotUtils;

public class TestUtil {
    public static Update createUpdateWithMessage(String text, long chatId) {
        String json = String.format("""
            {
                "update_id": 1,
                "message": {
                    "message_id": 123,
                    "from": {
                        "id": %d,
                        "is_bot": false,
                        "first_name": "Test",
                        "language_code": "en"
                    },
                    "chat": {
                        "id": %d,
                        "first_name": "Test",
                        "type": "private"
                    },
                    "date": 1614556800,
                    "text": "%s"
                }
            }
            """, chatId, chatId, text);

        return BotUtils.parseUpdate(json);
    }
}
