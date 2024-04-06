package minecraft.sightworld.defaultlib.user.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import minecraft.sightworld.defaultlib.user.UserData;

import java.sql.SQLException;

public class UserDao extends BaseDaoImpl<UserData, Long> {
    public UserDao(ConnectionSource connectionSource, Class<UserData> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
