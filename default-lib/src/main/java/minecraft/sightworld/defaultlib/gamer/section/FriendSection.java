package minecraft.sightworld.defaultlib.gamer.section;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.Getter;
import lombok.Setter;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;
import minecraft.sightworld.defaultlib.gamer.friends.Friend;
import minecraft.sightworld.defaultlib.gamer.friends.FriendAction;
import minecraft.sightworld.defaultlib.sql.GamerLoader;
import minecraft.sightworld.defaultlib.sql.api.table.ColumnType;
import minecraft.sightworld.defaultlib.sql.api.table.TableColumn;
import minecraft.sightworld.defaultlib.sql.api.table.TableConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
public class FriendSection extends Section {

    private final IntSet friends = new IntOpenHashSet();

    @Setter
    private int friendsLimit = 60; // TODO: Сделать лимиты в зависимости с группой игрока

    public FriendSection(IBaseGamer baseGamer) {
        super(baseGamer);
    }

    @Override
    public boolean loadData() {
        this.friends.addAll(GamerLoader.getFriends(baseGamer.getPlayerID()));
        return true;
    }

    public boolean isFriend(int playerID) {
        return friends.contains(playerID);
    }

    public boolean isFriend(@NotNull Friend friend) {
        return isFriend(friend.getPlayerId());
    }

    public void changeFriend(@NotNull FriendAction friendAction, Friend friend) {
        switch (friendAction) {
            case ADD_FRIEND -> friends.add(friend.getPlayerId());
            case REMOVE_FRIEND -> friends.remove(friend.getPlayerId());
        }
    }

    static {
        new TableConstructor("friends",
                new TableColumn("id", ColumnType.INT_11).primaryKey(true),
                new TableColumn("friend_id", ColumnType.INT_11).primaryKey(true)
        ).create(GamerLoader.getMysqlDatabase());
    }

}
