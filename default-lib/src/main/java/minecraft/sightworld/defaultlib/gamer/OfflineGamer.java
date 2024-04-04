package minecraft.sightworld.defaultlib.gamer;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import minecraft.sightworld.defaultlib.gamer.section.MoneySection;
import minecraft.sightworld.defaultlib.gamer.section.Section;
import minecraft.sightworld.defaultlib.sql.GamerLoader;

import java.net.InetAddress;
import java.util.Set;

@Getter
public class OfflineGamer extends IBaseGamerImpl {
    private static final ImmutableSet<Class<? extends Section>> LOADED_SECTIONS = ImmutableSet.of(
            MoneySection.class
    );

    OfflineGamer(String name) {
        super(name);

        getSections().values().forEach(Section::loadData);
        loadData();
    }

    private InetAddress lastIp;

    private String lastServer;

    private long lastOnline;

    private void loadData() {
        GamerLoader.getMysqlDatabase().executeQuery("SELECT * FROM `join_info` WHERE `id` = ? LIMIT 1;", (rs) -> {
            if (rs.next()) {
                this.lastServer = rs.getString("server");
                this.lastOnline = rs.getLong("last_online");
                this.lastIp = InetAddress.getByName(rs.getString("ip"));
            } else {
                this.lastServer = "unknown";
                this.lastOnline = 0;
                this.lastIp = InetAddress.getByName("0.0.0.0");
                GamerLoader.getMysqlDatabase().execute("INSERT INTO `join_info` (`id`, `ip`, `server`, `last_online`) VALUES (?, ?, ?, ?)",
                        getPlayerID(), "0.0.0.0", lastServer, lastOnline);
                //loadData();
            }
            return Void.TYPE;
        }, getPlayerID());
    }

    @Override
    protected Set<Class<? extends Section>> initSections() {
        return LOADED_SECTIONS;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void remove() {
        GamerAPI.removeOfflinePlayer(name);
    }

    @Override
    public boolean hasPermission(String permission) { // TODO: Доделать, пока что это нахуй не нужно
        return false;
    }


    @Override
    public String toString() {
        return "OfflineGamer{name=" + this.getName() + '}';
    }

    public String getChatName() {
        return getPrefix() + getName();
    }
}
