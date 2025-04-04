package gogo.gogostage.global.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.CacheKeyPrefix
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig(
    @Value("\${spring.data.redis.host}")
    private val host: String,
    @Value("\${spring.data.redis.port}")
    private val port: Int
) {
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisConfig = RedisStandaloneConfiguration(host, port)
        val clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(1))
            .shutdownTimeout(Duration.ZERO)
            .build()

        return LettuceConnectionFactory(redisConfig, clientConfig)
    }

      // 역작렬화 문제
//    @Bean
//    fun objectMapper(): ObjectMapper {
//        return ObjectMapper().apply {
//            registerModule(KotlinModule.Builder().build())
//            registerModule(JavaTimeModule())
//            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//        }
//    }

    @Bean
    fun cacheManager(): CacheManager {
        val serializer = GenericJackson2JsonRedisSerializer()
        val configuration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
            .entryTtl(Duration.ofHours(1))
            .disableCachingNullValues()

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory())
            .cacheDefaults(configuration)
            .build()
    }
}
