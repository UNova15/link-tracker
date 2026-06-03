package backend.academy.linktracker.scrapper.configuration;

import backend.academy.linktracker.scrapper.dto.linkdto.ListLinksResponse;
import com.github.benmanes.caffeine.cache.Cache;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import tools.jackson.databind.ObjectMapper;

@AllArgsConstructor
@Slf4j
public class ClusterClientSideCache implements DisposableBean {
    private final Cache<String, ListLinksResponse> cache;
    private final StatefulRedisClusterConnection<String, String> connection;

    private final ObjectMapper mapper;

    public ListLinksResponse get(String key) {
        ListLinksResponse value = cache.getIfPresent(key);

        if (value != null) {
            return value;
        }

        String remoteCache = connection.sync().get(key);

        if (remoteCache == null) {
            return null;
        }

        ListLinksResponse remoteValue = mapper.readValue(remoteCache, ListLinksResponse.class);
        cache.put(key, remoteValue);

        return remoteValue;
    }

    public void put(String key, ListLinksResponse value, Duration ttl) {
        String mappedValue = mapper.writeValueAsString(value);
        connection.sync().setex(key, ttl.getSeconds(), mappedValue);
    }

    @Override
    public void destroy() {
        if (connection != null) {
            connection.close();
        }
    }
}
