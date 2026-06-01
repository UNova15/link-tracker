package backend.academy.linktracker.bot.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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

    @PositiveOrZero
    int connectionTimeout;

    @PositiveOrZero
    int responseTimeout;
}
