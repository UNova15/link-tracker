package backend.academy.linktracker.scrapper.repository.orm.jparepository;

import backend.academy.linktracker.scrapper.repository.orm.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatJpaRepository extends JpaRepository<ChatEntity, Long> {}
