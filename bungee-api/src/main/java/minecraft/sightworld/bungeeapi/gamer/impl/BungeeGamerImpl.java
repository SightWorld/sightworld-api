package minecraft.sightworld.bungeeapi.gamer.impl;

import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.inventory.Inventory;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import minecraft.sightworld.bungeeapi.SightWorld;
import minecraft.sightworld.bungeeapi.gamer.BungeeGamer;
import minecraft.sightworld.bungeeapi.gamer.entity.BungeeEntity;
import minecraft.sightworld.bungeeapi.manager.LuckPermsManager;
import minecraft.sightworld.defaultlib.gamer.GamerBase;
import minecraft.sightworld.defaultlib.sound.SSound;
import minecraft.sightworld.defaultlib.sql.GamerLoader;
import minecraft.sightworld.defaultlib.sql.api.Database;
import minecraft.sightworld.defaultlib.sql.api.table.ColumnType;
import minecraft.sightworld.defaultlib.sql.api.table.TableColumn;
import minecraft.sightworld.defaultlib.sql.api.table.TableConstructor;
import minecraft.sightworld.defaultlib.utils.AddressUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE)
public class BungeeGamerImpl extends GamerBase implements BungeeEntity, BungeeGamer {

    final Database MY_SQL_DATABASE = GamerLoader.getMysqlDatabase();
    final InetAddress inetAddress;
    InetAddress lastIp;

    String lastServer;

    long lastOnline;

    @Setter
    boolean saved = true;



    static {
        new TableConstructor("join_info",
                new TableColumn("id", ColumnType.INT_11).primaryKey(true).autoIncrement(true).unigue(true),
                new TableColumn("ip", ColumnType.VARCHAR_64),
                new TableColumn("server", ColumnType.VARCHAR_64),
                new TableColumn("last_online", ColumnType.BIG_INT)
        ).create(GamerLoader.getMysqlDatabase());
    }

    public BungeeGamerImpl(final String name, final @NotNull InetAddress inetAddress) {
        super(name);

        this.inetAddress = inetAddress;
        MY_SQL_DATABASE.executeQuery("SELECT * FROM `join_info` WHERE `id` = ? LIMIT 1;", (rs) -> {
            if (rs.next()) {
                this.lastServer = rs.getString("server");
                this.lastOnline = rs.getLong("last_online");
                this.lastIp = InetAddress.getByName(rs.getString("ip"));
            } else {
                this.lastServer = "unknown";
                this.lastOnline = 0;
                this.lastIp = InetAddress.getByName("0.0.0.0");
                MY_SQL_DATABASE.execute("INSERT INTO `join_info` (`id`, `ip`, `server`, `last_online`) VALUES (?, ?, ?, ?)",
                        getPlayerID(), "0.0.0.0", "unknown", lastOnline);
                String prefix = LuckPermsManager.getPrefix(name);
                if (prefix != null) {
                    setPrefix(ChatColor.translateAlternateColorCodes('&', prefix), true);
                }
            }
            return Void.TYPE;
        }, getPlayerID());
    }

    public void disconnect(Server server) {
        val sessionIp = inetAddress.getHostAddress();

        lastServer = server == null ? "null" : server.getInfo().getName();

        MY_SQL_DATABASE.execute("UPDATE `join_info` SET `ip` = ?, `server` = ?, `last_online` = ? WHERE `id` = ?;",
                sessionIp, lastServer, System.currentTimeMillis(), getPlayerID());

        remove();
    }

    @Override
    public void sendTitle(Title title) {
        val player = getPlayer();
        if (player == null || title == null)
            return;

        player.sendTitle(title);
    }

    @Override
    public long getLastOnline() {
        return lastOnline;
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return ProxyServer.getInstance().getPlayer(name);
    }

    @Override
    public void sendMessage(BaseComponent component) {
        val player = ProxyServer.getInstance().getPlayer(getName());
        if (player != null && player.isConnected())
            player.sendMessage(component);

    }

    @Override
    public void sendMessage(String message) {
        val textComponent = new TextComponent(message);
        sendMessage(textComponent);
    }

    @Override
    public void sendMessage(BaseComponent[] components) {
        val player = ProxyServer.getInstance().getPlayer(getName());
        if (player != null && player.isConnected())
            player.sendMessage(components);
    }

    @Override
    public String getChatName() {
        return getPrefix() + getName();
    }

    @Override
    public String getIPLocation() {
        return AddressUtil.getLocationFromIP(getIp());
    }

    @Override
    public void processCommand(String command) {
        getPlayer().chat("/" + command);
    }

    @Override
    public void sendTitle(String title, String subTitle) {
        sendTitle(ProxyServer.getInstance().createTitle()
                .title(new TextComponent(title))
                .subTitle(new TextComponent(subTitle)));
    }

    @Override
    public void sendTitle(String title, String subTitle, long fadeIn, long stay, long fadeOut) {
        sendTitle(ProxyServer.getInstance().createTitle()
                .title(new TextComponent(title))
                .subTitle(new TextComponent(subTitle))
                .fadeIn((int) fadeIn)
                .fadeOut((int) fadeOut)
                .stay((int) stay));
    }

    @Override
    public void sendActionBar(String message) {
        val player = getPlayer();
        if (player == null)
            return;

        player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public InetAddress getIp() {
        return inetAddress;
    }

    @Override
    public void playSound(SSound sound) {
        SightWorld.getMessagingService().sendMessage(sound, "sound");
    }

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public boolean hasPermission(String permission) {
        return getPlayer().hasPermission(permission);
    }


    @Override
    public void disconnect(final @NotNull String reason) {
        getPlayer().disconnect(new TextComponent(reason));
    }
}
