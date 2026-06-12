package backend.academy.linktracker.scrapper.repository.orm.jparepository;

import backend.academy.linktracker.scrapper.domain.DBRecordStatus;
import backend.academy.linktracker.scrapper.repository.orm.entity.LinkEntity;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LinkJpaRepository extends JpaRepository<LinkEntity, Long> {
    void deleteByUrl(String url);

    Optional<LinkEntity> findByUrl(String url);

    @Query(value = """
        SELECT * FROM links
        WHERE status = 'IDLE'
        AND id > :lastCheckId
        AND last_check <= :delayTime
        ORDER BY id
        LIMIT :linksLimit
        FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    List<LinkEntity> findLinksToCheckWithDelayTime(
            @Param("lastCheckId") long lastCheckId,
            @Param("linksLimit") long linksLimit,
            @Param("delayTime") Instant delayTime);

    @Query("UPDATE LinkEntity l SET l.status =:status WHERE l.id IN :ids")
    @Modifying
    void updateStatus(@Param("ids") List<Long> ids, @Param("status") DBRecordStatus status);
}
