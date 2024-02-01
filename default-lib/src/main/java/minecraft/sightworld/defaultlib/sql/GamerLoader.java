package minecraft.sightworld.defaultlib.sql;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.experimental.UtilityClass;
import minecraft.sightworld.defaultlib.sql.api.Database;
import minecraft.sightworld.defaultlib.sql.api.StatementWrapper;
import minecraft.sightworld.defaultlib.sql.api.query.MysqlQuery;
import minecraft.sightworld.defaultlib.sql.api.query.QuerySymbol;
import minecraft.sightworld.defaultlib.utils.Pair;

import java.sql.PreparedStatement;
import java.util.regex.Pattern;

@UtilityClass
public class GamerLoader {

    private final Database MYSQL_DATABASE = Database.newBuilder()
            .data("sightworld")
            .host("localhost")
            .password("*23AmnasTmaj318.?%%DK391S")
            .user("root")
            .create();

    private final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    public Database getMysqlDatabase() {
        return MYSQL_DATABASE;
    }


    private final String GET_PLAYER_ID_QUERY = "SELECT `id` FROM `identifier` WHERE `player_name` = ? LIMIT 1";
    private final String GET_PLAYER_INFO = "SELECT * FROM `identifier` WHERE `player_name` = ? LIMIT 1";
    private final String GET_PLAYER_NAME_QUERY = "SELECT `player_name` FROM `identifier` WHERE `id` = ? LIMIT 1";
    private final String INSERT_PLAYER_ID = "INSERT INTO `identifier` (`player_name`) VALUES (?)";
    private final String GET_PREFIX_QUERY = "SELECT `prefix` FROM `prefixes` WHERE `id` = ? LIMIT 1;";
    private final String UPD_PREFIX_QUERY = "UPDATE `prefixes` SET `prefix` = ? WHERE `id` = ?;";
    private final String INS_PREFIX_QUERY = "INSERT INTO `prefixes` (`id`, `prefix`) VALUES (?, ?);";
    private final String GET_TAG_QUERY = "SELECT `tag` FROM `tags` WHERE `id` = ? LIMIT 1;";
    private final String UPD_TAG_QUERY = "UPDATE `tags` SET `tag` = ? WHERE `id` = ?;";
    private final String INS_TAG_QUERY = "INSERT INTO `tags` (`id`, `tag`) VALUES (?, ?);";
    private final String GET_DISCORD_ID_QUERY = "SELECT `discordID` FROM `discords` WHERE `id` = ? LIMIT 1;";
    private final String INS_DISCORD = "INSERT INTO `discords` (`id`, `discordId`) VALUES (?, ?);";
    private final String UPD_DISCORD = "UPDATE `discords` SET `discordID` = ? WHERE `id` = ?";
    private final String GET_VK_ID_QUERY = "SELECT `vkID` FROM `vk` WHERE `id` = ? LIMIT 1;";
    private final String INS_VK = "INSERT INTO `vk` (`id`, `vkID`) VALUES (?, ?);";
    private final String UPD_VK = "UPDATE `vk` SET `vkID` = ? WHERE `id` = ?";
    private final String GET_RUBY = "SELECT `ruby` FROM `rubies` WHERE `id` = ? LIMIT 1;";
    private final String INS_RUBY = "INSERT INTO `rubies` (`id`, `ruby`);";
    private final String UPD_CHANGE_RUBY = "UPDATE `rubies` SET `ruby` = `ruby` + ? WHERE `id` = ?;";
    private final String UPD_RUBY = "UPDATE `rubies` SET `ruby` = ? WHERE `id` = ?;";
    private final String GET_FRIENDS_ID_QUERY = "SELECT * FROM `friends` WHERE `id` = ?";



    public IntList getFriends(int playerID) {
        IntArrayList friends = new IntArrayList();

        if (playerID == -1) {
            return friends;
        }

        return MYSQL_DATABASE.executeQuery(GET_FRIENDS_ID_QUERY, (rs) -> {
            while (rs.next()) {
                friends.add(rs.getInt("friend_id"));
            }

            return friends;
        }, playerID);
    }


    public int getDiscordID(int playerID) {
        return MYSQL_DATABASE.executeQuery(GET_DISCORD_ID_QUERY, rs -> {
            if (rs.next()) {
                return rs.getInt("discordID");
            }

            return -1;
        }, playerID);
    }

    public int getRuby(int playerID) {
        return MYSQL_DATABASE.executeQuery(GET_RUBY, rs -> {
            if (rs.next()) {
                return rs.getInt("ruby");
            }

            return 0;
        }, playerID);
    }

    public String getPrefix(int playerID) {
        return MYSQL_DATABASE.executeQuery(GET_PREFIX_QUERY, rs -> {
            if (rs.next()) {
                return rs.getString("prefix");
            }

            return null;
        }, playerID);
    }


    public String getTag(int playerID) {
        return MYSQL_DATABASE.executeQuery(GET_TAG_QUERY, rs -> {
            if (rs.next()) {
                return rs.getString("prefix");
            }

            return null;
        }, playerID);
    }

    public Pair<String, Integer> getInfo(String name) {
        return MYSQL_DATABASE.executeQuery(GET_PLAYER_INFO, (rs) -> {
            Pair<String, Integer> pair = new Pair<>(name, -1);

            if (rs.next()) {
                pair = new Pair<>(rs.getString("player_name"), rs.getInt("id"));
            }

            return pair;
        }, name);
    }


    public String getChatName(int playerID) {
        if (playerID == -1) {
            return "§cError!";
        }

        return MYSQL_DATABASE.executeQuery(GET_PLAYER_NAME_QUERY, (rs) -> {
            String displayName = "§cError!";

            if (rs.next()) {
                displayName = rs.getString("player_name");
            }

            return "§r" + getPrefix(playerID) + displayName;
        }, playerID);
    }

    public String getDisplayName(int playerID) {
        if (playerID == -1) {
            return "§cError!";
        }

        return MYSQL_DATABASE.executeQuery(GET_PLAYER_NAME_QUERY, (rs) -> {
            String displayName = "§cError!";

            if (rs.next()) {
                displayName = rs.getString("player_name");
            }

            return "§r" + getPrefix(playerID) + displayName + getTag(playerID);
        }, playerID);
    }

    public String getName(int playerID) {
        if (playerID == -1) {
            return "";
        }

        return MYSQL_DATABASE.executeQuery(GET_PLAYER_NAME_QUERY,
                (rs) -> rs.next() ? rs.getString("player_name") : "", playerID);
    }


    public int getPlayerID(String name) {
        if (!NAME_PATTERN.matcher(name).matches() || name.length() < 3) {
            return -1;
        }

        return MYSQL_DATABASE.executeQuery(GET_PLAYER_ID_QUERY, (rs) -> {
            int playerID;

            if (rs.next()) {
                playerID = rs.getInt(1);
            } else {
                playerID = StatementWrapper
                        .create(MYSQL_DATABASE, INSERT_PLAYER_ID)
                        .execute(PreparedStatement.RETURN_GENERATED_KEYS, name);
            }

            return playerID;
        }, name);
    }

    public int containsPlayerID(String name) {
        if (!NAME_PATTERN.matcher(name).matches() || name.length() < 3) {
            return -1;
        }

        return MYSQL_DATABASE.executeQuery(GET_PLAYER_ID_QUERY,
                (rs) -> rs.next() ? rs.getInt(1) : -1, name);
    }

    public void changeRuby(int playerID, int value) {
        if (playerID == -1) {
            return;
        }

        MYSQL_DATABASE.executeQuery(GET_RUBY, (rs) -> {
            if (rs.next()) {
                MYSQL_DATABASE.execute(UPD_CHANGE_RUBY, value, playerID);
            } else {
                MYSQL_DATABASE.execute(INS_RUBY, playerID, value);
            }

            return Void.TYPE;
        }, playerID);
    }

    public void setDiscordID(int playerID, int value) {
        if (playerID == -1) {
            return;
        }

        MYSQL_DATABASE.executeQuery(GET_DISCORD_ID_QUERY, (rs) -> {
            if (rs.next()) {
                MYSQL_DATABASE.execute(UPD_DISCORD, value, playerID);
            } else {
                MYSQL_DATABASE.execute(INS_DISCORD, playerID, value);
                changeRuby(playerID, 2); // выдаем 2 рубина за привязку
            }

            return Void.TYPE;
        }, playerID);
    }

    public void setRuby(int playerID, int value) {
        if (playerID == -1) {
            return;
        }

        MYSQL_DATABASE.executeQuery(GET_RUBY, (rs) -> {
            if (rs.next()) {
                MYSQL_DATABASE.execute(UPD_RUBY, value, playerID);
            } else {
                MYSQL_DATABASE.execute(INS_RUBY, playerID, value);
            }

            return Void.TYPE;
        }, playerID);
    }

    public void setPrefix(int playerID, String value) {
        if (playerID == -1) {
            return;
        }

        MYSQL_DATABASE.executeQuery(GET_PREFIX_QUERY, (rs) -> {
            if (rs.next()) {
                MYSQL_DATABASE.execute(UPD_PREFIX_QUERY, value, playerID);
            } else {
                MYSQL_DATABASE.execute(INS_PREFIX_QUERY, playerID, value);
            }

            return Void.TYPE;
        }, playerID);
    }

    public void setTag(int playerID, String value) {
        if (playerID == -1) {
            return;
        }

        MYSQL_DATABASE.executeQuery(GET_TAG_QUERY, (rs) -> {
            if (rs.next()) {
                MYSQL_DATABASE.execute(UPD_TAG_QUERY, value, playerID);
            } else {
                MYSQL_DATABASE.execute(INS_TAG_QUERY, playerID, value);
            }

            return Void.TYPE;
        }, playerID);
    }

    public void setSetting(int playerID, int type, int value, boolean insert) {
        if (playerID == -1) {
            return;
        }

        if (insert) {
            MYSQL_DATABASE.execute(MysqlQuery.insertTo("settings")
                    .set("id", playerID)
                    .set("setting_id", type)
                    .set("setting_value", value)
            );
            return;
        }

        MYSQL_DATABASE.execute(MysqlQuery.update("settings")
                .where("id", QuerySymbol.EQUALLY, playerID)
                .where("setting_id", QuerySymbol.EQUALLY, type)
                .set("setting_value", value));
    }



}
