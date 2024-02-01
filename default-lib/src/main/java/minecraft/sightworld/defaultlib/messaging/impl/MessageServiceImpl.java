package minecraft.sightworld.defaultlib.messaging.impl;

import minecraft.sightworld.defaultlib.messaging.MessageListener;
import minecraft.sightworld.defaultlib.messaging.MessageService;
import org.redisson.api.RedissonClient;

import java.util.Arrays;

public class MessageServiceImpl implements MessageService {
    private final RedissonClient redissonClient;

    public MessageServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void sendMessage(Object o, String... channels) {
        Arrays.stream(channels).forEach(channel -> redissonClient.getTopic(channel).publishAsync(o));
    }

    @Override
    public void addListener(MessageListener<?> listener, String... channels) {
        Arrays.stream(channels).forEach(channel -> redissonClient.getTopic(channel).addListenerAsync(listener.getMessageClass(), toType(listener)));
    }

    @SuppressWarnings("ignore all")
    protected <T> T toType(Object o) {
        try {
            return (T) o;
        } catch (ClassCastException e) {
            return null;
        }
    }
}

