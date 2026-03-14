package backend.academy.linktracker.scrapper.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.telegram-bot")
@Validated
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class TelegramBotProperties {

    @NotEmpty
    private String baseUrl;

    @NotEmpty
    private String updateEndpoint;
}
