package minecraft.sightworld.defaultlib.user.service;

import com.google.common.cache.LoadingCache;
import minecraft.sightworld.defaultlib.group.Group;
import minecraft.sightworld.defaultlib.user.User;
import minecraft.sightworld.defaultlib.user.UserData;

import java.util.Map;

public interface UserService<T> {
    Map<String, ? extends User<T>> users();
    LoadingCache<String, ? extends User<T>> offlineCache();
    User<T> loadUser(String name);
    User<T> createUser(UserData userData);
    void saveUser(User<T> user);
    User<T> getUser(String name);
    User<T> getOrCreate(String name);
    void addOnlineUser(User<T> user);
    void removeOnlineUser(String user);
    void addOfflineUser(User<T> user);
    void removeOfflineUser(String user);
    void disconnectUser(User<T> user, String server);
    void joinUser(User<T> user, String ip);
    default UserData buildDefaultUserData(String name) {
        UserData userData = new UserData();
        userData.setName(name);
        userData.setPrefix(Group.DEFAULT.getPrefix());
        userData.setGroup(Group.DEFAULT.name());
        userData.setTag("");

        return userData;
    }
}
