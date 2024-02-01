package minecraft.sightworld.defaultlib.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class DefaultRedisFactory implements RedisFactory {
    @Override
    public RedissonClient create() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }
}
