package minecraft.sightworld.defaultlib.gamer.section;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;
import minecraft.sightworld.defaultlib.sql.GamerLoader;
import minecraft.sightworld.defaultlib.sql.api.table.ColumnType;
import minecraft.sightworld.defaultlib.sql.api.table.TableColumn;
import minecraft.sightworld.defaultlib.sql.api.table.TableConstructor;
import minecraft.sightworld.defaultlib.utils.Pair;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LinkSection extends Section {

    int discordId = -1;

    public LinkSection(IBaseGamer baseGamer) {
        super(baseGamer);
    }

    @Override
    public boolean loadData() {
        int playerID = baseGamer.getPlayerID();

        if (baseGamer.isOnline() && playerID != -1) {
            int discordResult = GamerLoader.getDiscordID(playerID);
            if (discordResult != -1) {
                this.discordId = discordResult;
            }
        }

        return true;
    }

    public void setDiscordID(int value) {
        GamerLoader.setDiscordID(baseGamer.getPlayerID(), value);
        this.discordId = value;
    }

    static {
        new TableConstructor("discords",
                new TableColumn("id", ColumnType.INT_11).unigue(true).primaryKey(true),
                new TableColumn("discordID", ColumnType.INT_11)
        ).create(GamerLoader.getMysqlDatabase());
        new TableConstructor("vk", // TODO: Доделать
                new TableColumn("id", ColumnType.INT_11).unigue(true).primaryKey(true),
                new TableColumn("vkID", ColumnType.INT_11)
        ).create(GamerLoader.getMysqlDatabase());
    }
}
