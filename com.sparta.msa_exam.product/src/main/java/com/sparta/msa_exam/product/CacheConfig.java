package com.sparta.msa_exam.product;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory) {
        // 설정 구성을 먼저 진행
        // Redis를 이용해서 Spring Cache를 사용할 때
        // Redis 관련 설정을 모아둔 클래스
        RedisCacheConfiguration configuration = RedisCacheConfiguration
                .defaultCacheConfig()
                // null을 캐싱하는지
                .disableCachingNullValues()
                // 기본 캐시 유지시간
                .entryTtl(Duration.ofSeconds(60))
                // 캐시를 구분하는 접두사 설정
                .computePrefixWith(CacheKeyPrefix.simple())
                // 캐시에 저장할 값을 어떻게 직렬화/역 직렬화 할 것 인지 설정
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.java())
                );

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(configuration)
                .build();
    }
}
