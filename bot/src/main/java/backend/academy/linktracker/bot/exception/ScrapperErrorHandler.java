package backend.academy.linktracker.bot.exception;

import backend.academy.linktracker.bot.model.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class ScrapperErrorHandler {
    private final ObjectMapper mapper;

    public ScrapperErrorHandler() {
        this.mapper = new ObjectMapper();
    }

    public void handleError(HttpRequest request, ClientHttpResponse response) throws IOException {
        ApiErrorResponse errorResponse = mapper.readValue(response.getBody().readAllBytes(), ApiErrorResponse.class);
        throw new ScrapperException(errorResponse);
    }
}
