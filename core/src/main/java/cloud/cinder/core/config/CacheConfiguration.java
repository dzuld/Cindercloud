package cloud.cinder.core.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final RedisSerializer serializer = new StringRedisSerializer();

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisCacheManager redisCacheManager(JedisConnectionFactory jedisConnectionFactory) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate(jedisConnectionFactory));
        redisCacheManager.setUsePrefix(true);
        redisCacheManager.setCachePrefix(cacheName -> serializer.serialize((String.format("cc.%s:", cacheName))));
        redisCacheManager.setDefaultExpiration(300);
        return redisCacheManager;
    }

}
