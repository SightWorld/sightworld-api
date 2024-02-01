package minecraft.sightworld.defaultlib.messaging;

public interface MessageListener<P, M> {
    void onMessage(P player, String messageChannel, M message);

    Class<M> getMessageClass();
}
