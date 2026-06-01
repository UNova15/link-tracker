package backend.academy.linktracker.scrapper.linksclient;

import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowResponse;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StackOverflowService {
    private final StackOverflowClient stackOverflowClient;

    @Retry(name = "stackoverflow")
    public StackOverflowResponse sendURequestForUpdates(long id, long lastCheck) {
        return stackOverflowClient.sendURequestForUpdates(id, lastCheck);
    }
}
