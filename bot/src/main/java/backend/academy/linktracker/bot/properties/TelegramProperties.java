package backend.academy.linktracker.bot.properties;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.telegram")
@Validated
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class TelegramProperties {

    @NotEmpty
    @URL
    private String url;

    @NotEmpty
    private String token;

    @NotNull
    private Duration connectionTimeout;

    // Время для чтения/записи ответов тг при long pooling
    @NotNull
    private Duration writeTimeout;

    @NotNull
    private Duration readTimeout;

    @NotNull
    private Duration updateListenerSleep;

    private boolean debug;
}
