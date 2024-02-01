package minecraft.sightworld.defaultlib.messaging;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseMessagingService<P> implements MessagingService<P> {
    protected final Map<String, List<MessageListener<P, ?>>> listenerMap = new HashMap<>();
    protected final Map<String, Class<?>> subChannelMap = new HashMap<>();
    protected final Gson gson;
    protected final JsonParser jsonParser;

    protected BaseMessagingService(Gson gson, JsonParser jsonParser) {
        this.gson = gson;
        this.jsonParser = jsonParser;
    }

    @SuppressWarnings("unchecked")
    protected  <T> T convertObject(Object o) {
        try {
            return (T) o;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
