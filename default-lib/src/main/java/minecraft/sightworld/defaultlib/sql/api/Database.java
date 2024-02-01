package minecraft.sightworld.defaultlib.sql.api;

import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.Builder;
import minecraft.sightworld.defaultlib.sql.api.query.Query;

import java.sql.*;

public class Database implements AbstractDatabase {
    //Основные параметры подключения
    private String host, password, user, data;

    //MySQL
    private MysqlDataSource dataSource;

    //Подключение
    private Connection connection = null;

    @Builder(builderMethodName = "newBuilder", builderClassName = "MySqlBuilder", buildMethodName = "create")
    private Database(String host,
                          String password,
                          String user,
                          String data) {

        this.host = host;
        this.password = password;
        this.user = user;
        this.data = data;

        this.dataSource = configureDataSource(new MysqlDataSource());
    }


    private MysqlDataSource configureDataSource(MysqlDataSource source) {
        source.setDatabaseName(this.data);
        source.setPassword(this.password);
        source.setUser(this.user);
        source.setServerName(this.host);
        source.setPort(3306);
        try {
            source.setAutoReconnect(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return source;
    }

    @Override
    public int execute(Query query) {
        return execute(query.toString());
    }

    @Override
    public <T> T executeQuery(Query query, ResponseHandler<ResultSet, T> handler) {
        return executeQuery(query.toString(), handler);
    }

    @Override
    public int execute(String query, Object... objects) {
        return execute(StatementWrapper.create(this, query), objects);
    }

    @Override
    public <T> T executeQuery(String query, ResponseHandler<ResultSet, T> handler, Object... objects) {
        return executeQuery(StatementWrapper.create(this, query), handler, objects);
    }

    @Override
    public int execute(StatementWrapper wrapper, Object... objects) {
        return wrapper.execute(PreparedStatement.RETURN_GENERATED_KEYS, objects);
    }

    @Override
    public <T> T executeQuery(StatementWrapper wrapper, ResponseHandler<ResultSet, T> handler, Object... objects) {
        return wrapper.executeQuery(handler, objects);
    }

    @Override
    public Connection getConnection() {
        refreshConnection();
        return connection;
    }

    protected void refreshConnection() {
        try {
            if (connection != null && !connection.isClosed() && connection.isValid(1000)) {
                return;
            }

            this.connection = DriverManager.getConnection("JDBC:mysql://"
                    + this.host + ":3306/" + this.data + "?autoReconnect=true", this.user, this.password);;
        } catch (SQLException e) {
            throw new RuntimeException("Все нахуй обосралось с MySql - " + this.host + "/" + this.data, e);
        }
    }

    @Override
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            //nothing
        }
    }
}
