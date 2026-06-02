package backend.academy.linktracker.scrapper.util;

import backend.academy.linktracker.scrapper.configuration.ClusterClientSideCache;
import backend.academy.linktracker.scrapper.dto.linkdto.ListLinksResponse;
import backend.academy.linktracker.scrapper.properties.CacheProperties;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.lettuce.core.TrackingArgs;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class ClusterClientSideCacheFactory {
    private static final String INVALIDATE_COMMAND = "invalidate";
    private final ObjectMapper mapper;

    public ClusterClientSideCache create(RedisConnectionFactory redisConnectionFactory, CacheProperties properties) {
        Cache<String, ListLinksResponse> localCache = createLocalCache(properties);

        LettuceConnectionFactory factory = (LettuceConnectionFactory) redisConnectionFactory;
        RedisClusterClient client = (RedisClusterClient) factory.getNativeClient();

        if (client == null) {
            throw new RuntimeException("Error to create redis cluster client");
        }

        StatefulRedisClusterConnection<String, String> connection = client.connect();

        connection.addListener((redisClusterNode, pushMessage) -> {
            if (INVALIDATE_COMMAND.equals(pushMessage.getType())) {
                List<Object> content = pushMessage.getContent();

                if (content.size() > 1 && content.get(1) instanceof List<?> keys) {

                    for (Object key : keys) {
                        ByteBuffer duplicate = ((ByteBuffer) key).duplicate();
                        byte[] bytes = new byte[duplicate.remaining()];
                        duplicate.get(bytes);
                        String keyToInvalidate = new String(bytes, StandardCharsets.UTF_8);

                        localCache.invalidate(keyToInvalidate);
                    }
                }
            }
        });
        connection.sync().upstream().commands().clientTracking(TrackingArgs.Builder.enabled());
        return new ClusterClientSideCache(localCache, connection, mapper);
    }

    public Cache<String, ListLinksResponse> createLocalCache(CacheProperties properties) {
        return Caffeine.newBuilder()
                .maximumSize(properties.valkey().maxSize())
                .expireAfterWrite(properties.valkey().ttl())
                .build();
    }
}
