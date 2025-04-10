package gogo.gogostage.global.cache

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisCacheService(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    fun deleteFromCache(key: String) {
        redisTemplate.delete(key)
    }

}
