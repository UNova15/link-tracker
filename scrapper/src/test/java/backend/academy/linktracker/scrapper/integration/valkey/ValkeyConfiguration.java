package backend.academy.linktracker.scrapper.integration.valkey;

import static backend.academy.linktracker.scrapper.integration.valkey.SubscriptionServiceCacheTest.valkey;

import io.lettuce.core.internal.HostAndPort;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DnsResolvers;
import io.lettuce.core.resource.MappingSocketAddressResolver;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class ValkeyClusterTestConfig {
    @Bean(destroyMethod = "shutdown")
    public ClientResources clientResources() {
        return ClientResources.builder()
                .socketAddressResolver(MappingSocketAddressResolver.create(
                        DnsResolvers.UNRESOLVED,
                        hostAndPort -> HostAndPort.of(valkey.getHost(), valkey.getFirstMappedPort())))
                .build();
    }
}
