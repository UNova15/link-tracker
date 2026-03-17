package backend.academy.linktracker.bot.exception;

import backend.academy.linktracker.bot.model.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class ScrapperErrorHandler {
    private final ObjectMapper mapper;

    public ScrapperErrorHandler() {
        this.mapper = new ObjectMapper();
    }

    public void handleScrapperClientError(HttpRequest request, ClientHttpResponse response) throws IOException {
        ApiErrorResponse errorResponse = mapper.readValue(response.getBody().readAllBytes(), ApiErrorResponse.class);
        throw new ScrapperClientException(errorResponse);
    }

    public void handleScrapperServerError(HttpRequest request, ClientHttpResponse response) throws IOException {
        ApiErrorResponse errorResponse = mapper.readValue(response.getBody().readAllBytes(), ApiErrorResponse.class);
        throw new ScrapperServerException(errorResponse);
    }
}
