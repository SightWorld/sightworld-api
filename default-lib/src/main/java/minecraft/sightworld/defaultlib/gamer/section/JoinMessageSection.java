package minecraft.sightworld.defaultlib.gamer.section;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import minecraft.sightworld.defaultlib.gamer.IBaseGamer;
import minecraft.sightworld.defaultlib.gamer.constants.JoinMessage;
import minecraft.sightworld.defaultlib.sql.GamerLoader;
import minecraft.sightworld.defaultlib.sql.api.query.MysqlQuery;
import minecraft.sightworld.defaultlib.sql.api.query.QuerySymbol;
import minecraft.sightworld.defaultlib.sql.api.table.ColumnType;
import minecraft.sightworld.defaultlib.sql.api.table.TableColumn;
import minecraft.sightworld.defaultlib.sql.api.table.TableConstructor;

import java.util.ArrayList;
import java.util.List;

public class JoinMessageSection extends Section {

    private JoinMessage joinMessage;

    private final Int2ObjectMap<JoinMessage> available = new Int2ObjectOpenHashMap<>();

    public JoinMessageSection(IBaseGamer baseGamer) {
        super(baseGamer);
    }

    @Override
    public boolean loadData() {
        int playerId = baseGamer.getPlayerID();
        GamerLoader.getMysqlDatabase().executeQuery(MysqlQuery.selectFrom("join_messages")
                .where("id", QuerySymbol.EQUALLY, playerId)
                .limit(JoinMessage.getMessages().size()), (rs) -> {

            while (rs.next()) {
                JoinMessage joinMessage = JoinMessage.getMessage(rs.getInt("message"));
                available.putIfAbsent(joinMessage.getId(), joinMessage);

                if (rs.getBoolean("available")) {
                    this.joinMessage = joinMessage;
                }
            }
            return Void.TYPE;
        });

        if (baseGamer.isDonater()) {
            available.putIfAbsent(JoinMessage.DEFAULT_MESSAGE.getId(), JoinMessage.DEFAULT_MESSAGE);
        }

        if (baseGamer.isWither()) {
            for (JoinMessage joinMessage : JoinMessage.getMessages()) {
                if (!joinMessage.isWither()) {
                    continue;
                }

                available.putIfAbsent(joinMessage.getId(), joinMessage);
            }
        }

        return true;
    }

    public JoinMessage getJoinMessage() {
        if (!baseGamer.isDonater()) {
            return null;
        }

        if (joinMessage == null) {
            return JoinMessage.DEFAULT_MESSAGE;
        }

        return joinMessage;
    }

    public void setDefaultJoinMessage(JoinMessage joinMessage, boolean mysql) {
        if (joinMessage == null || this.joinMessage == joinMessage)
            return;

        if (mysql) {
            int playerId = baseGamer.getPlayerID();

            if (this.joinMessage != null) {
                GamerLoader.getMysqlDatabase().execute(MysqlQuery.update("join_messages") //старое отключить
                        .set("available", 0)
                        .where("id", QuerySymbol.EQUALLY, playerId)
                        .where("message", QuerySymbol.EQUALLY, this.joinMessage.getId()).limit());
            }

            GamerLoader.getMysqlDatabase().executeQuery(MysqlQuery.selectFrom("join_messages")
                    .where("id", QuerySymbol.EQUALLY, playerId)
                    .where("message", QuerySymbol.EQUALLY, joinMessage.getId())
                    .limit(), (rs) -> {
                if (rs.next()) {
                    GamerLoader.getMysqlDatabase().execute(MysqlQuery.update("join_messages")
                            .set("available", 1)
                            .where("id", QuerySymbol.EQUALLY, playerId)
                            .where("message", QuerySymbol.EQUALLY, joinMessage.getId()).limit());
                } else {
                    GamerLoader.getMysqlDatabase().execute(MysqlQuery.insertTo("join_messages")
                            .set("id", playerId)
                            .set("message", joinMessage.getId())
                            .set("available", 1));
                }

                return Void.TYPE;
            });
        }

        this.joinMessage = joinMessage;
    }

    public List<JoinMessage> getAvailableJoinMessage() {
        return new ArrayList<>(available.values());
    }

    public void addJoinMessage(JoinMessage joinMessage, boolean mysql) {
        if (joinMessage == null || available.containsKey(joinMessage.getId())) {
            return;
        }

        available.put(joinMessage.getId(), joinMessage);
        if (mysql) {
            GamerLoader.getMysqlDatabase().execute(MysqlQuery.insertTo("join_messages")
                    .set("id", baseGamer.getPlayerID())
                    .set("message", joinMessage.getId()));
        }
    }


    static {
        new TableConstructor("join_messages",
                new TableColumn("id", ColumnType.INT_11).primaryKey(true),
                new TableColumn("message", ColumnType.INT_5).primaryKey(true),
                new TableColumn("available", ColumnType.BOOLEAN).setDefaultValue(0)
        ).create(GamerLoader.getMysqlDatabase());
    }
}
