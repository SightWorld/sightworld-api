package minecraft.sightworld.defaultlib.messaging;

import java.lang.reflect.ParameterizedType;

public abstract class BaseMessageListener<P, M> implements MessageListener<P, M> {
    @Override
    @SuppressWarnings("ignore all")
    public Class<M> getMessageClass() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class) type.getActualTypeArguments()[1];
    }
}
