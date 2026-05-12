package backend.academy.linktracker.scrapper.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgreSQLContainer() {
        Network network = Network.newNetwork();
        String dbAlias = "db-" + UUID.randomUUID();

        PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres:18.3-trixie")
            .withNetwork(network)
            .withNetworkAliases(dbAlias)
            .withDatabaseName("scrapper_db")
            .withUsername("user")
            .withPassword("password");

        dbContainer.start();

        runMigrations(dbContainer, network, dbAlias);

        return dbContainer;
    }

    private void runMigrations(PostgreSQLContainer dbContainer, Network network, String dbAlias) {
        try (GenericContainer<?> liquibase = new GenericContainer<>(
            new ImageFromDockerfile()
                .withFileFromPath("Dockerfile", Paths.get("../migrations/Dockerfile")))) {

            liquibase
                .withNetwork(network)
                .withCopyFileToContainer(MountableFile.forHostPath("../migrations"), "/liquibase/changelog")
                .withEnv("LIQUIBASE_COMMAND_URL", "jdbc:postgresql://" + dbAlias + ":5432/" + dbContainer.getDatabaseName())
                .withEnv("LIQUIBASE_COMMAND_USERNAME", dbContainer.getUsername())
                .withEnv("LIQUIBASE_COMMAND_PASSWORD", dbContainer.getPassword())
                .withEnv("LIQUIBASE_COMMAND_CHANGELOG_FILE", "changelog-master.xml")
                .withCommand("update");

            liquibase.waitingFor(
                Wait.forLogMessage(".*(successfully|Unexpected error|ERROR:).*\\s", 1)
                    .withStartupTimeout(Duration.ofMinutes(2))
            );

            liquibase.start();
        }
    }
}
