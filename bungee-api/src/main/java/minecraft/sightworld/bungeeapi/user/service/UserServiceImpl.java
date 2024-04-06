package minecraft.sightworld.bungeeapi.user.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import minecraft.sightworld.bungeeapi.user.ProxiedUser;
import minecraft.sightworld.defaultlib.group.Group;
import minecraft.sightworld.defaultlib.user.User;
import minecraft.sightworld.defaultlib.user.UserData;
import minecraft.sightworld.defaultlib.user.dao.UserDao;
import minecraft.sightworld.defaultlib.user.service.UserService;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService<ProxiedPlayer> {

    private final UserDao userDao;

    private final Map<String, ProxiedUser> users = new ConcurrentHashMap<>();

    private final LoadingCache<String, ProxiedUser> offlineCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public @NotNull ProxiedUser load(@NotNull String key) {
                    return getUser(key);
                }
            });

    @Override
    public Map<String, ProxiedUser> users() {
        return users;
    }

    @Override
    public LoadingCache<String, ProxiedUser> offlineCache() {
        return offlineCache;
    }

    @Override
    public ProxiedUser loadUser(String name) {
        List<UserData> userData;
        try {
            userData = userDao.queryForEq("name", name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Optional<UserData> userDataOptional = userData.stream().findFirst();
        if (userDataOptional.isPresent()) {
            UserData data = userDataOptional.get();
            return new ProxiedUser(data.getName(), data);
        }
        return null;
    }


    @Override
    public ProxiedUser createUser(UserData data) {
        ProxiedUser proxiedUser = new ProxiedUser(data.getName(), data);
        try {
            userDao.create(data);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return proxiedUser;
    }

    @Override
    public void saveUser(User<ProxiedPlayer> user) {
        if (user instanceof ProxiedUser proxiedUser) {
            try {
                userDao.update(proxiedUser.getUserData());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported user type");
        }
    }


    @Override
    public ProxiedUser getUser(String name) {
        try {
            return offlineCache.get(name.toLowerCase());
        } catch (ExecutionException e) {
            ProxiedUser proxiedUser = users.get(name.toLowerCase());
            if (proxiedUser == null) {
                return loadUser(name);
            } else {
                return proxiedUser;
            }
        }
    }

    @Override
    public ProxiedUser getOrCreate(String name) {
        ProxiedUser proxiedUser = getUser(name);
        if (proxiedUser == null) {
            UserData userData = new UserData();

            userData.setName(name);
            userData.setPrefix(Group.DEFAULT.getPrefix());

            proxiedUser = createUser(userData);
        }
        return proxiedUser;
    }

    @Override
    public void addOnlineUser(User<ProxiedPlayer> user) {
        users.put(user.getName().toLowerCase(), (ProxiedUser) user);
    }

    @Override
    public void removeOnlineUser(String user) {
        users.remove(user.toLowerCase());
    }

    @Override
    public void addOfflineUser(User<ProxiedPlayer> user) {
        offlineCache.put(user.getName().toLowerCase(), (ProxiedUser) user);
    }

    @Override
    public void removeOfflineUser(String user) {
        offlineCache.invalidate(user.toLowerCase());
    }
}
