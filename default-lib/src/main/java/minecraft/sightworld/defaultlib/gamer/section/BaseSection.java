package minecraft.sightworld.defaultlib.gamer.section;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import minecraft.sightworld.defaultlib.utils.Pair;
import minecraft.sightworld.defaultlib.sql.GamerLoader;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;
import minecraft.sightworld.defaultlib.sql.api.table.ColumnType;
import minecraft.sightworld.defaultlib.sql.api.table.TableColumn;
import minecraft.sightworld.defaultlib.sql.api.table.TableConstructor;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseSection extends Section {

    int playerID = -1;
    String prefix = "§7";
    String tag = "";

    public BaseSection(IBaseGamer baseGamer) {
        super(baseGamer);
    }

    @Override
    public boolean loadData() {
        Pair<String, Integer> playerInfo = GamerLoader.getInfo(baseGamer.getName());
        playerID = playerInfo.getSecond();

        if (baseGamer.isOnline()) {
            if (playerID == -1) {
                playerID = GamerLoader.getPlayerID(baseGamer.getName());
            }
        } else {
            baseGamer.setName(playerInfo.getFirst());
        }

        if (playerID != -1) {
            String tagResult = GamerLoader.getTag(playerID);
            if (tagResult != null) {
                tag = tagResult;
            }

            String prefixResult = GamerLoader.getPrefix(playerID);
            if (prefixResult != null) {
                prefix = prefixResult;
            }
        }
        return playerID != -1;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void savePrefix(String prefix) {
        this.prefix = prefix;
        GamerLoader.setPrefix(baseGamer.getPlayerID(), prefix);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void saveTag(String tag) {
        this.tag = tag;
        GamerLoader.setTag(baseGamer.getPlayerID(), tag);
    }

    static {
        new TableConstructor("identifier",
                new TableColumn("id", ColumnType.INT_11).primaryKey(true).autoIncrement(true).unigue(true),
                new TableColumn("player_name", ColumnType.VARCHAR_16)
        ).create(GamerLoader.getMysqlDatabase());
        new TableConstructor("prefixes",
                new TableColumn("id", ColumnType.INT_11).primaryKey(true).unigue(true),
                new TableColumn("prefix", ColumnType.VARCHAR_32)
        ).create(GamerLoader.getMysqlDatabase());
        new TableConstructor("tags",
                new TableColumn("id", ColumnType.INT_11).primaryKey(true).unigue(true),
                new TableColumn("tag", ColumnType.VARCHAR_32)
        ).create(GamerLoader.getMysqlDatabase());
    }
}
