package minecraft.sightworld.defaultlib.redis;

import org.redisson.api.RedissonClient;

public interface RedisFactory {
    RedissonClient create();
}
