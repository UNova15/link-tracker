package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.telegramservice.BotUpdateListener;
import backend.academy.linktracker.bot.properties.TelegramProperties;
import com.pengrad.telegrambot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class TelegramBotConfiguration {

    @Bean
    public TelegramBot telegramBot(TelegramProperties properties) {
        var builder = new TelegramBot.Builder(properties.getToken())
                .apiUrl(properties.getUrl())
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
