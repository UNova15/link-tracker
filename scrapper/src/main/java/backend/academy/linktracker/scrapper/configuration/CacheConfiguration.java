package backend.academy.linktracker.scrapper.configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import backend.academy.linktracker.scrapper.properties.CacheProperties;
import backend.academy.linktracker.scrapper.util.ClusterClientSideCacheFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory, @Value("${app.cache-ttl}") long ttl) {
        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.builder()
                .enableUnsafeDefaultTyping()
                .build();

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.of(ttl, ChronoUnit.SECONDS))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(factory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    @Bean
    public ClusterClientSideCache clusterClientSideCache(
            RedisConnectionFactory redisConnectionFactory,
            ClusterClientSideCacheFactory factory,
            CacheProperties properties) {
        return factory.create(redisConnectionFactory, properties);
    }
}
