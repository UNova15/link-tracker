package backend.academy.linktracker.scrapper.integration.database;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.integration.TestcontainersConfiguration;
import backend.academy.linktracker.scrapper.linktracker.LinkTracker;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest("spring.main.lazy-initialization=true")
@Transactional
@Import(TestcontainersConfiguration.class)
public abstract class AbstractLinkRepositoryTest {

    @Autowired
    protected LinkRepository linkRepository;

    @MockitoBean
    protected LinkTracker linkTracker;

    @Test
    protected void save_withValidLink_saveLink() {
        Link link = Link.createNew(LinkType.GIT_HUB, "https://github.com");

        linkRepository.save(link);

        Optional<Link> actualLinkOpt = linkRepository.findByUrl(link.getUrl());

        assertThat(actualLinkOpt).isPresent();
        Link actualLink = actualLinkOpt.get();

        assertThat(actualLink.getType()).isEqualTo(link.getType());
        assertThat(actualLink.getUrl()).isEqualTo(link.getUrl());
        assertThat(actualLink.getLastCheck()).isEqualTo(link.getLastCheck());
    }

    @Test
    protected void save_withEqualsLinksUrl_throwException() {
        Link link1 = Link.createNew(LinkType.GIT_HUB, "https://github.com");
        Link link2 = Link.createNew(LinkType.STACK_OVERFLOW, "https://github.com");

        linkRepository.save(link1);

        assertThatThrownBy(() -> linkRepository.save(link2)).isInstanceOf(DataAccessException.class);
    }

    @Test
    protected void findLinksFilteredByDelayTime_withNonUpdatedLinks_returnUnupdatedLinks() {
        long lastCheckId = 0;
        long linksLimit = 10;

        Link link1 = Link.createNew(LinkType.GIT_HUB, "https://github.com1");
        Link link2 = Link.createNew(LinkType.STACK_OVERFLOW, "https://github.com2");
        Link link3 = Link.createNew(LinkType.GIT_HUB, "https://github.com3");

        linkRepository.save(link1);
        linkRepository.save(link2);
        linkRepository.save(link3);

        Instant delayTime = Instant.now().plusSeconds(5);

        List<Link> links = linkRepository.findLinksFilteredByDelayTime(lastCheckId, linksLimit, delayTime);

        assertThat(links)
                .hasSize(3)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrder(link1, link2, link3);
    }

    @Test
    protected void findLinksFilteredByDelayTime_withUpdatedLinks_returnUnupdatedLinks() {
        long lastCheckId = 0;
        long linksLimit = 10;

        Link link1 = Link.createNew(LinkType.GIT_HUB, "https://github.com1");
        Link link2 = Link.createNew(LinkType.STACK_OVERFLOW, "https://github.com2");
        Link link3 = Link.createNew(LinkType.GIT_HUB, "https://github.com3");

        linkRepository.save(link1);
        linkRepository.save(link2);
        linkRepository.save(link3);

        Instant delayTime = Instant.now().minusSeconds(5);

        List<Link> links = linkRepository.findLinksFilteredByDelayTime(lastCheckId, linksLimit, delayTime);

        assertThat(links).isEmpty();
    }

    @Test
    protected void findAllByIdIn_withExistsId_returnListOfLinks() {
        Link link1 = Link.createNew(LinkType.GIT_HUB, "https://github.com1");
        Link link2 = Link.createNew(LinkType.STACK_OVERFLOW, "https://github.com2");
        Link link3 = Link.createNew(LinkType.GIT_HUB, "https://github.com3");
        Link link4 = Link.createNew(LinkType.GIT_HUB, "https://github.com4");

        Link expectedLink1 = linkRepository.save(link1);
        Link expectedLink2 = linkRepository.save(link2);
        Link expectedLink3 = linkRepository.save(link3);
        Link expectedLink4 = linkRepository.save(link4);

        List<Long> linkIds = List.of(expectedLink1.getId(), expectedLink2.getId(), expectedLink3.getId());

        List<Link> actualLinks = linkRepository.findAllByIdIn(linkIds);

        assertThat(actualLinks)
                .hasSize(3)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrder(link1, link2, link3);
    }

    @Test
    protected void findAllByIdIn_withNonExistsId_returnListOfLinks() {
        List<Long> linkIds = List.of(1L, 2L, 4L);

        List<Link> actualLinks = linkRepository.findAllByIdIn(linkIds);

        assertThat(actualLinks).isEmpty();
    }

    @Test
    protected void update_withValidLink_updateLastCheckForLinkLink() {
        int linkCount = 5;
        List<Link> links = new ArrayList<>();
        List<Instant> creationTimes = new ArrayList<>();

        for (int i = 0; i < linkCount; i++) {
            Link link = Link.createNew(LinkType.GIT_HUB, "https://github.com" + i);
            creationTimes.add(link.getLastCheck());
            Link savedLink = linkRepository.save(link);
            links.add(savedLink);
        }

        for (var link : links) {
            link.markCheckedNow();
        }

        linkRepository.updateLastCheckForLink(links);

        for (int i = 0; i < linkCount; i++) {
            Link link = links.get(i);
            Optional<Link> actualLinkOpt = linkRepository.findByUrl(link.getUrl());

            assertThat(actualLinkOpt).isPresent();
            Link actualLink = actualLinkOpt.get();

            assertThat(actualLink.getType()).isEqualTo(link.getType());
            assertThat(actualLink.getUrl()).isEqualTo(link.getUrl());
            assertThat(actualLink.getLastCheck()).isEqualTo(link.getLastCheck());
            assertThat(actualLink.getLastCheck()).isAfter(creationTimes.get(i));
        }
    }

    @Test
    protected void removeByUrl_deleteExistLink_deleteLink() {
        Link link = Link.createNew(LinkType.GIT_HUB, "https://github.com");
        linkRepository.save(link);

        linkRepository.removeByUrl(link.getUrl());

        Optional<Link> actualLink = linkRepository.findByUrl(link.getUrl());

        assertThat(actualLink).isEmpty();
    }
}
