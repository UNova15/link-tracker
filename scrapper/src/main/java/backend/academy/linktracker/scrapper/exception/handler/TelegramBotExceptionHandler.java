package backend.academy.linktracker.scrapper.exception.handler;

import backend.academy.linktracker.scrapper.exception.ApiErrorResponse;
import backend.academy.linktracker.scrapper.exception.TelegramBotException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TelegramBotExceptionHandler {
    private final ObjectMapper mapper;

    public TelegramBotExceptionHandler() {
        this.mapper = new ObjectMapper();
    }

    public void handleTelegramError(HttpRequest request, ClientHttpResponse response) throws IOException {
        ApiErrorResponse resp = mapper.readValue(response.getBody().readAllBytes(), ApiErrorResponse.class);
        throw new TelegramBotException(resp);
    }
}
