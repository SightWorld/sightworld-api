package minecraft.sightworld.defaultlib.messaging;

public interface MessagingService<P> {
    void addListener(MessageListener<P, ?> messageListener, String... channels);

    void sendMessage(P player, Object o, String... channels);
}
