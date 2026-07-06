package com.example.profile.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final RedisConnectionFactory connectionFactory;

    @Bean
    public ApplicationRunner redisConnectionChecker() {
        return args -> {
            try (var connection = connectionFactory.getConnection()) {
                String pong = connection.ping();
                log.info("Redis connected successfully. PING -> {}", pong);
            } catch (Exception e) {
                log.error("Failed to connect to Redis: {}", e.getMessage());
            }
        };
    }

    @Bean
    RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());

        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.json());

        return template;
    }
}
