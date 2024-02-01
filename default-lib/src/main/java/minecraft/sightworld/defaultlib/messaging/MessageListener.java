package minecraft.sightworld.defaultlib.messaging;

public interface MessageListener<Message> extends org.redisson.api.listener.MessageListener<Message> {
    @Override
    void onMessage(CharSequence charSequence, Message message);

    Class<Message> getMessageClass();
}
