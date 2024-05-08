package ru.alex.databases;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class C3P0pool {
    private static ComboPooledDataSource pool = new ComboPooledDataSource();

//    static {
//        pool.setDriverClass();
//    }
//    необходим вызов метода close у объекта Connection для того, чтобы вернуть соединение в пул
    public static Connection getConnection() throws SQLException {
        return pool.getConnection();
    }
}
