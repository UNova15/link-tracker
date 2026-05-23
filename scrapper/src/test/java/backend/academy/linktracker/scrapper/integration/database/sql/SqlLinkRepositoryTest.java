package backend.academy.linktracker.scrapper.integration.database.sql;

import backend.academy.linktracker.scrapper.integration.database.AbstractLinkRepositoryTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "app.db-provider=sql")
public class SqlLinkRepositoryTest extends AbstractLinkRepositoryTest {}
