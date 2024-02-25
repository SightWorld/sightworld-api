package minecraft.sightworld.defaultlib.gamer.section;

import minecraft.sightworld.defaultlib.gamer.IBaseGamer;
import minecraft.sightworld.defaultlib.sql.GamerLoader;
import minecraft.sightworld.defaultlib.sql.api.table.ColumnType;
import minecraft.sightworld.defaultlib.sql.api.table.TableColumn;
import minecraft.sightworld.defaultlib.sql.api.table.TableConstructor;

public class GroupSection extends Section {
    public GroupSection(IBaseGamer baseGamer) {
        super(baseGamer);
    }

    @Override
    public boolean loadData() {


        return true;
    }



    static {
        new TableConstructor("groups",
                new TableColumn("id", ColumnType.INT_11).primaryKey(true).autoIncrement(true).unigue(true),
                new TableColumn("group", ColumnType.VARCHAR_64).setDefaultValue(0)
        ).create(GamerLoader.getMysqlDatabase());
    }
}
