package backend.academy.linktracker.scrapper;

import backend.academy.linktracker.scrapper.integration.TestContainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestScrapperApplication {

    static void main(String[] args) {
        SpringApplication.from(ScrapperApplication::main)
                .with(TestContainersConfiguration.class)
                .run(args);
    }
}
