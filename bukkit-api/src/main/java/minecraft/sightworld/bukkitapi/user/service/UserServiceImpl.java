package minecraft.sightworld.bukkitapi.user.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import minecraft.sightworld.bukkitapi.user.BukkitUser;
import minecraft.sightworld.defaultlib.group.Group;
import minecraft.sightworld.defaultlib.user.User;
import minecraft.sightworld.defaultlib.user.UserData;
import minecraft.sightworld.defaultlib.user.dao.UserDao;
import minecraft.sightworld.defaultlib.user.service.UserService;
import minecraft.sightworld.defaultlib.user.session.UserSession;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService<Player> {

    private final UserDao userDao;

    private final Map<String, BukkitUser> users = new ConcurrentHashMap<>();

    private final LoadingCache<String, BukkitUser> offlineCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull BukkitUser load(@NotNull String key) {
                    return loadUser(key);
                }
            });

    @Override
    public Map<String, BukkitUser> users() {
        return users;
    }

    @Override
    public LoadingCache<String, BukkitUser> offlineCache() {
        return offlineCache;
    }

    @Override
    public BukkitUser loadUser(String name) {
        List<UserData> userData;
        try {
            userData = userDao.queryForEq("name", name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Optional<UserData> userDataOptional = userData.stream().findFirst();
        if (userDataOptional.isPresent()) {
            UserData data = userDataOptional.get();
            return new BukkitUser(data.getName(), data);
        }
        return null;
    }


    @Override
    public BukkitUser createUser(UserData data) {
        BukkitUser proxiedUser = new BukkitUser(data.getName(), data);
        try {
            userDao.assignEmptyForeignCollection(data, "sessions");
            userDao.create(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return proxiedUser;
    }

    @Override
    public void saveUser(User<Player> user) {
        if (user instanceof BukkitUser bukkitUser) {
            try {
                userDao.update(bukkitUser.getUserData());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported user type");
        }
    }


    @Override
    public BukkitUser getUser(String name) {
        try {
            return offlineCache.get(name.toLowerCase(), () -> {
                BukkitUser onlineUser = users.get(name.toLowerCase());
                if (onlineUser == null) {
                    return loadUser(name);
                } else {
                    return onlineUser;
                }
            });
        }catch (Exception e) {
           return null;
        }
    }

    @Override
    public BukkitUser getOrCreate(String name) {
        BukkitUser bukkitUser = getUser(name);
        if (bukkitUser == null) {
            UserData userData = new UserData();

            userData.setName(name);
            userData.setPrefix(Group.DEFAULT.getPrefix());

            bukkitUser = createUser(userData);
        }
        return bukkitUser;
    }

    @Override
    public void addOnlineUser(User<Player> user) {
        users.put(user.getName().toLowerCase(), (BukkitUser) user);
    }

    @Override
    public void removeOnlineUser(String user) {
        users.remove(user.toLowerCase());
    }

    @Override
    public void addOfflineUser(User<Player> user) {
        offlineCache.put(user.getName().toLowerCase(), (BukkitUser) user);
    }

    @Override
    public void removeOfflineUser(String user) {
        offlineCache.invalidate(user.toLowerCase());
    }

    @Override
    public void disconnectUser(User<Player> user, String server) {

        removeOnlineUser(user.getName());
        addOfflineUser(user);

    }

    @Override
    public void joinUser(User<Player> user, String ip) {

        addOnlineUser(user);
    }
}
