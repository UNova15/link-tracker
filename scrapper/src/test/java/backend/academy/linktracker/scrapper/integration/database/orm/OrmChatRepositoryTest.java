package backend.academy.linktracker.scrapper.integration.database.orm;

import backend.academy.linktracker.scrapper.integration.database.AbstractChatRepositoryTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "app.db-provider=orm")
public class OrmChatRepositoryTest extends AbstractChatRepositoryTest {}
