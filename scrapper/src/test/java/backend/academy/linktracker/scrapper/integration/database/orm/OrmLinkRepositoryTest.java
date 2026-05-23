package backend.academy.linktracker.scrapper.integration.database.orm;

import static org.assertj.core.api.Assertions.assertThat;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.integration.database.AbstractLinkRepositoryTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "app.db-provider=orm")
public class OrmLinkRepositoryTest extends AbstractLinkRepositoryTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private EntityManager entityManager;

    @DynamicPropertySource
    static void registerHibernateProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.properties.hibernate.generate_statistics", () -> true);
        registry.add("spring.jpa.properties.hibernate.jdbc.batch_size", () -> 50);
        registry.add("spring.jpa.properties.hibernate.order_updates", () -> true);
    }

    @Test
    protected void update_withValidLink_usesBatchUpdate() {
        int countOfLinks = 5;
        List<Link> links = new ArrayList<>();

        for (int i = 0; i < countOfLinks; i++) {
            Link link = Link.createNew(LinkType.GIT_HUB, "https://github.com" + i);
            Link savedLink = linkRepository.save(link);
            links.add(savedLink);
        }

        entityManager.flush();

        for (var link : links) {
            link.markCheckedNow();
        }

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        statistics.clear();

        linkRepository.updateLastCheckAndLastUpdate(links);

        entityManager.flush();

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
        assertThat(statistics.getEntityUpdateCount()).isEqualTo(countOfLinks);
    }

    @Test
    protected void findLinksFilteredByDelayTime_withUpdatedLinks_usesBatchRead() {
        int countOfLinks = 5;
        int lastCheckId = 0;
        List<Link> links = new ArrayList<>();

        for (int i = 0; i < countOfLinks; i++) {
            Link link = Link.createNew(LinkType.GIT_HUB, "https://github.com" + i);
            Link savedLink = linkRepository.save(link);
            links.add(savedLink);
        }

        entityManager.flush();
        entityManager.clear();

        Instant delayTime = Instant.now().plusSeconds(5);

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        statistics.clear();

        linkRepository.findLinksFilteredByDelayTime(lastCheckId, countOfLinks, delayTime);

        assertThat(statistics.getPrepareStatementCount()).isEqualTo(1);
        assertThat(statistics.getEntityLoadCount()).isEqualTo(countOfLinks);
    }
}
