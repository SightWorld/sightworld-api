package minecraft.sightworld.defaultlib.database;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import minecraft.sightworld.defaultlib.user.UserData;
import minecraft.sightworld.defaultlib.user.dao.UserDao;
import minecraft.sightworld.defaultlib.user.session.UserSession;

import java.sql.SQLException;

@Getter
public class Database {

    private UserDao userDao;
    public JdbcPooledConnectionSource getConnectionSource () {
        JdbcPooledConnectionSource connectionSource;
        try {
            //connectionSource = new JdbcPooledConnectionSource("jdbc:mysql://localhost:3306/sightworld?useSSL=false&serverTimezone=UTC");
            connectionSource = new JdbcPooledConnectionSource("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=UTC");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connectionSource.setUsername("root");
        connectionSource.setPassword("");
        //connectionSource.setPassword("*23AmnasTmaj318.?%%DK391S");
        return connectionSource;
    }

    public void load() throws SQLException {
        JdbcPooledConnectionSource connectionSource = getConnectionSource();

        createTables(connectionSource);
        createDao(connectionSource);
    }

    public void createTables(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, UserData.class);
        TableUtils.createTableIfNotExists(connectionSource, UserSession.class);
    }

    public void createDao(ConnectionSource connectionSource) throws SQLException {
        userDao = new UserDao(connectionSource, UserData.class);
    }
}
