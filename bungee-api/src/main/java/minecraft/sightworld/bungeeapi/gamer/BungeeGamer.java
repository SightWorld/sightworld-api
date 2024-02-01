package minecraft.sightworld.bungeeapi.gamer;

import dev.simplix.protocolize.api.inventory.Inventory;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import lombok.val;
import minecraft.sightworld.bungeeapi.gamer.impl.BungeeGamerImpl;
import minecraft.sightworld.defaultlib.gamer.GamerAPI;
import minecraft.sightworld.defaultlib.gamer.GamerBase;
import minecraft.sightworld.defaultlib.gamer.OnlineGamer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.Map;

public interface BungeeGamer extends OnlineGamer {

    static BungeeGamer getGamer(final @NotNull ProxiedPlayer player) {
        return getGamer(player.getName());
    }

    static BungeeGamer getGamer(final @NotNull String name) {
        return (BungeeGamer) GamerAPI.getByName(name);
    }

    static GamerBase getOnlineGamer(final @NotNull String name) {
        return GamerAPI.getOnline(getGamer(name).getPlayerID());
    }

    static BungeeGamer getGamer(final int id) {
        return (BungeeGamer) GamerAPI.getGamers().values()
                .stream()
                .filter(gamerBase -> gamerBase.getPlayerID() == id)
                .findFirst()
                .orElse(null);
    }

    static Map<String, GamerBase> getGamers() {
        return GamerAPI.getGamers();
    }

    /**
     * Создать игрока в апи
     * @param name ник
     * @param inetAddress его ип адрес при входе
     */
    static @NotNull BungeeGamer getOrCreate(
            @NotNull final String name,
            @NotNull final InetAddress inetAddress
    ) {
        val gamer = (BungeeGamer) GamerAPI.getByName(name);
        if (gamer != null)
            return gamer;

        return new BungeeGamerImpl(name, inetAddress);
    }

    /**
     * Получить игрока
     */
    ProxiedPlayer getPlayer();

    /**
     * Отправить тайтл игроку
     */
    void sendTitle(Title title);

    /*
    были ли изменены данные игрока или нет
     */
    boolean isSaved();
    void setSaved(boolean saved);

    InetAddress getLastIp();
    String getLastServer();

    long getLastOnline();
    InetAddress getIp();


    ProtocolizePlayer getProtocolPlayer();
    void openInventory(Inventory inventory);



    /**
     * переписать всю инфу об игроке в БД
     */
    void disconnect(Server lastServer);

    void disconnect(String reason);
    String getChatName();
    String getIPLocation();

    void processCommand(String command);
}
