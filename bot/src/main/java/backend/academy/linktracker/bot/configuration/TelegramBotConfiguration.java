package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.properties.TelegramProperties;
import backend.academy.linktracker.bot.telegramservice.BotUpdateListener;
import com.pengrad.telegrambot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TelegramBotConfiguration {
    public static final String TELEGRAM_CONFIG_NAME = "telegram";

    @Bean
    public TelegramBot telegramBot(TelegramProperties properties) {
        var builder = new TelegramBot.Builder(properties.getToken())
                .apiUrl(properties.getUrl())
                .okHttpClient(new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(properties.getConnectionTimeout())
                        .readTimeout(properties.getReadTimeout())
                        .writeTimeout(properties.getWriteTimeout())
                        .build())
                .updateListenerSleep(properties.getUpdateListenerSleep().toMillis());

        if (properties.isDebug()) {
            log.info("Запуск в режиме debug");
            builder.debug();
        }

        return builder.build();
    }

    @Bean
    public InitializingBean registerUpdateListener(TelegramBot bot, BotUpdateListener listener) {
        return () -> bot.setUpdatesListener(listener);
    }
}
