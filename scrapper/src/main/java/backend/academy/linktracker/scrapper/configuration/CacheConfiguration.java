package backend.academy.linktracker.scrapper.configuration;

import backend.academy.linktracker.scrapper.properties.CacheProperties;
import backend.academy.linktracker.scrapper.util.ClusterClientSideCacheFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory, CacheProperties properties) {
        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.builder()
                .enableUnsafeDefaultTyping()
                .build();

        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(properties.ttl())
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
