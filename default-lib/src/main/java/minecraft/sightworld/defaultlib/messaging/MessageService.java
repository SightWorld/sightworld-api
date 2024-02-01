package minecraft.sightworld.defaultlib.messaging;

public interface MessageService {
    void sendMessage(Object o, String... channels);

    void addListener(MessageListener<?> listener, String... channels);
}
