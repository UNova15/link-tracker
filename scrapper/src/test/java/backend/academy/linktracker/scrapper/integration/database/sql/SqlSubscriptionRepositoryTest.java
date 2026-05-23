package backend.academy.linktracker.scrapper.integration.database.sql;

import backend.academy.linktracker.scrapper.integration.database.AbstractSubscriptionRepositoryTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "app.db-provider=sql")
public class SqlSubscriptionRepositoryTest extends AbstractSubscriptionRepositoryTest {}
