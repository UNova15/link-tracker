package backend.academy.linktracker.bot.configuration;

import backend.academy.linktracker.bot.properties.CacheProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.UUID;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(CacheProperties properties) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("buckets");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(properties.rateLimiter().maxSize())
                .expireAfterAccess(properties.rateLimiter().ttl()));
        return cacheManager;
    }

    @Bean
    public RedisTemplate<UUID, Boolean> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<UUID, Boolean> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new GenericToStringSerializer<>(UUID.class));
        template.setValueSerializer(new GenericToStringSerializer<>(Boolean.class));
        return template;
    }
}
