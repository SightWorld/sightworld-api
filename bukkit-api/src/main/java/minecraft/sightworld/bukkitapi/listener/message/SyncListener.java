package minecraft.sightworld.bukkitapi.listener.message;

import lombok.RequiredArgsConstructor;
import minecraft.sightworld.bukkitapi.user.BukkitUser;
import minecraft.sightworld.defaultlib.messaging.impl.BaseMessageListener;
import minecraft.sightworld.defaultlib.user.SyncUserData;
import minecraft.sightworld.defaultlib.user.UserData;
import minecraft.sightworld.defaultlib.user.service.UserService;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

@RequiredArgsConstructor
public class SyncListener extends BaseMessageListener<SyncUserData> {

    private final UserService<Player> userService;
    @Override
    public void onMessage(CharSequence charSequence, SyncUserData syncUserData) {
        BukkitUser user = (BukkitUser) userService.getOnlineUser(syncUserData.getUser());
        System.out.println("Пытаюсь синхронизировать данные с пользователем " + syncUserData.getUser());
        if (user == null) {
            System.out.println("Пользователь не найден");
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
