package backend.academy.linktracker.scrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class ScrapperApplication {

    static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}

/**
 * TODO план (Успеть за 2 - 3 дня)
 * 1)описание миграций, проектирование бд +
 * 2)Имплементация ORM реализации: +
 *      1) вынести общие интерфейсы
 *      2) создание специфичных JPA репозиториев
 *      3) Создание специфичных JPA сущностей
 * 3) Имплементация RawSql реализации: +
 *      1) Если сущность не получится унифицировать то создать новые
 *      2) Аналогично для интерфейсов
 * 4) ConditionalOnProperty для условного создания бинов для JPA и для RawSQL реализаций
 * 5) Перенести условие фильтрации ссылок по тегам в SQL запрос
 * 6) Используется тестирование тест контейнер (покрыть сначала 3 потом 2 дз)
 * 7) костыль с jar файлом driver Liquibase
 */
