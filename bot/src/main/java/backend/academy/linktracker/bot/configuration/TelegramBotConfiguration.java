package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.properties.TelegramProperties;
import backend.academy.linktracker.bot.client.telegram.BotUpdateListener;
import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotConfiguration.class);

    @Bean
    public TelegramBot telegramBot(TelegramProperties properties) {
        var builder = new TelegramBot.Builder(properties.getToken())
                .apiUrl(properties.getUrl())
                .updateListenerSleep(properties.getUpdateListenerSleep().toMillis());

        if (properties.isDebug()) {
            logger.info("Запуск в режиме debug");
            builder.debug();
        }

        return builder.build();
    }

    @Bean
    public InitializingBean registerUpdateListener(TelegramBot bot, BotUpdateListener listener) {
        return () -> bot.setUpdatesListener(listener);
    }
}
