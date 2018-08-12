package com.borzdykooa.connection;

import com.borzdykooa.util.PropertyManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;

/*
Класс, отвечающий за соединение с базой данных. Без сторонней библиотеки так и не смогла нормально сделать,
пришлось использовать классы org.apache.tomcat
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConnectionPool {

    private static DataSource DATA_SOURCE;

    static {
        initConnectionPool();
    }

    private static void initConnectionPool() {
        PoolProperties poolProperties = new PoolProperties();
        poolProperties.setDriverClassName(PropertyManager.get("db.driver"));
        poolProperties.setUrl(PropertyManager.get("db.url"));
        poolProperties.setUsername(PropertyManager.get("db.user"));
        poolProperties.setPassword(PropertyManager.get("db.password"));
        poolProperties.setMaxActive(Integer.parseInt(PropertyManager.get("db.pool.size")));
        poolProperties.setMaxIdle(Integer.parseInt(PropertyManager.get("db.max.idle")));
        DATA_SOURCE = new DataSource(poolProperties);
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
