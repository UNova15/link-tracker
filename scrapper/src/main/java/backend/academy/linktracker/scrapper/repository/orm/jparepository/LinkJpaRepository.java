package backend.academy.linktracker.scrapper.repository.orm.jparepository;

import backend.academy.linktracker.scrapper.repository.orm.entity.LinkEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkJpaRepository extends JpaRepository<LinkEntity, Long> {
    void deleteByUrl(String url);

    Optional<LinkEntity> findByUrl(String url);

    @Query(value = """
        SELECT * FROM links
        WHERE id > :lastCheckId
        AND last_check <= :delayTime
        ORDER BY id
        LIMIT :linksLimit
        """, nativeQuery = true)
    List<LinkEntity> findLinksToCheckWithDelayTime(
            @Param("lastCheckId") long lastCheckId,
            @Param("linksLimit") long linksLimit,
            @Param("delayTime") Instant delayTime);
}
