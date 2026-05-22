package backend.academy.linktracker.scrapper.linksclient;

import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/questions/{id}")
public interface StackOverflowClient {

    @GetExchange
    StackOverflowResponse sendURequestForUpdates(@PathVariable("id") long id, @RequestParam("fromdate") long lastCheck);
}
