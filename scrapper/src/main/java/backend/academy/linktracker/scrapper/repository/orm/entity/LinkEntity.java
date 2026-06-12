package backend.academy.linktracker.scrapper.repository.orm.entity;

import backend.academy.linktracker.scrapper.domain.DBRecordStatus;
import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "links")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LinkType type;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "last_check", nullable = false)
    private Instant lastCheck;

    @Column(name = "last_update", nullable = false)
    private Instant lastUpdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DBRecordStatus status;

    public static LinkEntity fromDomain(Link link) {
        return new LinkEntity(
                link.getId(),
                link.getType(),
                link.getUrl(),
                link.getLastCheck(),
                link.getLastUpdate(),
                // значение по умолчанию для всех новых ссылок
                DBRecordStatus.IDLE);
    }
}
