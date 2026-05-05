package backend.academy.linktracker.scrapper.repository.orm.jparepository;

import backend.academy.linktracker.scrapper.repository.orm.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LinkJpaRepository extends JpaRepository<LinkEntity, Long> {
    void deleteByUrl(String url);

    Optional<LinkEntity> findByUrl(String url);
}
