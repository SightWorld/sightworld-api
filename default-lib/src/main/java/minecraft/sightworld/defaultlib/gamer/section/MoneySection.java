package minecraft.sightworld.defaultlib.gamer.section;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;
import minecraft.sightworld.defaultlib.sql.GamerLoader;
import minecraft.sightworld.defaultlib.sql.api.table.ColumnType;
import minecraft.sightworld.defaultlib.sql.api.table.TableColumn;
import minecraft.sightworld.defaultlib.sql.api.table.TableConstructor;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MoneySection extends Section {

    int ruby = 0; // рубины
    int money = 0;

    public MoneySection(IBaseGamer baseGamer) {
        super(baseGamer);
    }

    @Override
    public boolean loadData() {
        int playerID = baseGamer.getPlayerID();
        if (baseGamer.isOnline() && playerID != -1) {
            int rubyResult = GamerLoader.getRuby(playerID);

            if (rubyResult != 0) {
                ruby = rubyResult;
            }
        }
        return true;
    }

    public void changeRuby(int value) {
        if (ruby + value < 0) {
            return;
        }

        GamerLoader.changeRuby(baseGamer.getPlayerID(), value);
        this.ruby = ruby + value;
    }

    public void setRuby(int value) {
        if (value < 0) {
            return;
        }

        GamerLoader.setRuby(baseGamer.getPlayerID(), value);
        this.ruby = value;
    }


    static {
        new TableConstructor("rubies",
                new TableColumn("id", ColumnType.INT_11).primaryKey(true).autoIncrement(true).unigue(true),
                new TableColumn("ruby", ColumnType.INT_5).setDefaultValue(0)
        ).create(GamerLoader.getMysqlDatabase());
    }
}
