package backend.academy.linktracker.bot.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.scrapper")
@Validated
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ScrapperProperties {

    @NotBlank
    @URL
    String baseUrl;

    @NotNull
    Duration connectionTimeout;

    @NotNull
    Duration responseTimeout;
}
