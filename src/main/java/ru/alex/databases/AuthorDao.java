package ru.alex.databases;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorDao {
    public boolean createTable(){
        String createSql = "CREATE TABLE IF NOT EXISTS tb_authors (" +
                "id SERIAL PRIMARY KEY, " +
                "unique_name VARCHAR(50) NOT NULL, " +
                "registered_at DATE DEFAULT CURRENT_DATE NOT NULL, " +
                "is_active BOOLEAN DEFAULT TRUE NOT NULL)";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // import java.sql.*
        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/my_base1",
                "alex86",
                "megafon1986"
        )){
            try (Statement statement = connection.createStatement()){
                // executeUpdate: создание, обновление, удаление
                // таблиц или записей
                // для не SELECT запросов
                statement.executeUpdate(createSql);
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(Author author){
        String insertSql = "INSERT INTO tb_authors (unique_name, is_active)" +
                "VALUES (?, ?)"; /*  вместо ? будут подставляться данные */

        try(Connection connection = C3P0pool.getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(insertSql)){

                ps.setString(1, author.getUniqueName());
                ps.setBoolean(2, author.isActive());
                return ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public int[] insert(List<Author> authors){
        String insertSql = "INSERT INTO tb_authors (unique_name, is_active)" +
                "VALUES (?, ?)"; /*  вместо ? будут подставляться данные */

        try(Connection connection = C3P0pool.getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(insertSql)){
                for (Author author : authors) {
                    ps.setString(1, author.getUniqueName());
                    ps.setBoolean(2, author.isActive());
                    ps.addBatch();
                }
                return ps.executeBatch(); /* Когда выполняем пачками */
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public int update(Author author){
        String insertSql = "UPDATE tb_authors SET is_active = ?" +
                "WHERE unique_name = ?"; /*  вместо ? будут подставляться данные */

        try(Connection connection = C3P0pool.getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(insertSql)){
                ps.setBoolean(1, author.isActive());
                ps.setString(2, author.getUniqueName());
                return ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Author getByUniqueName(String uniqueName){
        String selectSql = "SELECT id, unique_name, " +
                "registered_at AS registered, is_active " +
                "FROM tb_authors " +
                "WHERE unique_name = ?";
        try (Connection connection = C3P0pool.getConnection()){
            try (PreparedStatement ps = connection.prepareStatement(selectSql)){
                ps.setString(1, uniqueName);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    Author author = new Author();
                    author.setId(resultSet.getInt("id"));
                    author.setUniqueName(resultSet.getString("unique_name"));
                    author.setRegisteredAt(resultSet.getObject("registered", LocalDate.class));
                    // Boolean b = resultSet.getBoolean("is_active");
                    author.setActive(resultSet.getBoolean("is_active"));
                    return author;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Author> allAuthors(){
        String selectSql = "SELECT id, unique_name, registered_at, is_active" +
                "FROM tb_authors WHERE is_active = true";
        List<Author> authors = new ArrayList<>();
        try(Connection connection = C3P0pool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(selectSql)){
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()){
                    Author author = new Author();
                    author.setId(resultSet.getInt("id"));
                    author.setUniqueName(resultSet.getString("unique_name"));
                    author.setRegisteredAt(resultSet.getObject("registered_at", LocalDate.class));
                    author.setActive(resultSet.getBoolean("is_active"));
                    authors.add(author);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return authors;
    }
}
