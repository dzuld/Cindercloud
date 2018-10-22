package cloud.cinder.core.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final RedisSerializer serializer = new StringRedisSerializer();

    @Bean
    public RedisCacheConfiguration redisCacheManager() {
        return RedisCacheConfiguration.defaultCacheConfig().prefixKeysWith("cc.")
                .entryTtl(Duration.of(300, ChronoUnit.SECONDS)).serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
    }
}
