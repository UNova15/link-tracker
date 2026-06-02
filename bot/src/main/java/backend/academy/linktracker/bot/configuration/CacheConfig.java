package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.properties.CacheProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.UUID;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(CacheProperties properties) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("buckets", "idempotency-key");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(properties.rateLimiter().maxSize())
                .expireAfterAccess(properties.rateLimiter().ttl()));
        return cacheManager;
    }

    @Bean
    public Cache<UUID, Boolean> idempotencyKeys(CacheProperties properties) {
        return Caffeine.newBuilder()
                .maximumSize(properties.idempotencyKey().maxSize())
                .expireAfterAccess(properties.idempotencyKey().ttl())
                .build();
    }
}
