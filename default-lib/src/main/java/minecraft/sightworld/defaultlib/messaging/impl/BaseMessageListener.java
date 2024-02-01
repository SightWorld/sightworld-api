package minecraft.sightworld.defaultlib.messaging.impl;


import minecraft.sightworld.defaultlib.messaging.MessageListener;

import java.lang.reflect.ParameterizedType;

public abstract class BaseMessageListener<Message> implements MessageListener<Message> {

    @Override
    @SuppressWarnings("ignore all")
    public Class<Message> getMessageClass() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class) type.getActualTypeArguments()[0];
    }
}
