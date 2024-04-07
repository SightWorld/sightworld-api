package minecraft.sightworld.bungeeapi.listener;

import lombok.RequiredArgsConstructor;
import minecraft.sightworld.bungeeapi.user.ProxiedUser;
import minecraft.sightworld.defaultlib.messaging.impl.BaseMessageListener;
import minecraft.sightworld.defaultlib.user.SyncUserData;
import minecraft.sightworld.defaultlib.user.UserData;
import minecraft.sightworld.defaultlib.user.service.UserService;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class SyncListener extends BaseMessageListener<SyncUserData> {

    private final UserService<ProxiedPlayer> userService;
    @Override
    public void onMessage(CharSequence charSequence, SyncUserData syncUserData) {
        ProxiedUser user = (ProxiedUser) userService.getOnlineUser(syncUserData.getUser());
        if (user == null) {
            return;
        }
        UserData userData = user.getUserData();
        Class<?> objClass = userData.getClass();

        Field field;
        try {
            field = objClass.getDeclaredField(syncUserData.getField());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        field.setAccessible(true);

        try {
            field.set(userData, syncUserData.getValue());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(false);
        }
    }
}
