package ru.alex.databases;

import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class NotesDao {
    public boolean create(){
        String createSql = "CREATE TABLE IF NOT EXISTS tb_notes(" +
                "id SERIAL PRIMARY KEY, " +
                "title VARCHAR(120) NOT NULL, " +
                "text TEXT NOT NULL, " +
                "created_at TIMESTAMPTZ CHECK (created_at < CURRENT_TIMESTAMP) NOT NULL, " +
                "author_id INTEGER NOT NULL, " +
                "CONSTRAINT fk_author_notes " +
                "FOREIGN KEY (author_id) " +
                "REFERENCES tb_authors (id))";

        try (Connection connection = C3P0pool.getConnection()){
            try (Statement statement = connection.createStatement()){
                statement.executeUpdate(createSql);
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(Note note){
        String insertSql = "INSERT INTO tb_notes (title, text, created_at, author_id)" +
                "VALUES (?, ?, ?, ?)"; /*  вместо ? будут подставляться данные */

        try(Connection connection = C3P0pool.getConnection()) {
            try(PreparedStatement ps = connection.prepareStatement(insertSql)){
                ps.setString(1, note.getTitle());
                ps.setString(2, note.getText());
                ps.setObject(3 ,note.getCreateAt());
                ps.setInt(4, note.getAuthor().getId());
                return ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Author getAuthorById(int id){
        String selectSql = "SELECT id, unique_name, " +
                "registered_at AS registered, is_active " +
                "FROM tb_authors " +
                "WHERE id = ?";
        try (Connection connection = C3P0pool.getConnection()){
            try (PreparedStatement ps = connection.prepareStatement(selectSql)){
                ps.setInt(1, id);
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

    public Note getById(long id){
        String selectSql = "SELECT id, title, text, created_at, author_id " +
                "FROM tb_notes " +
                "WHERE id = ?";
        try(Connection connection = C3P0pool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(selectSql)){
                ps.setLong(1, id);
                ResultSet resultSet = ps.executeQuery();
                if(resultSet.next()){
                    Note note = new Note();
                    note.setId(resultSet.getLong("id"));
                    note.setTitle(resultSet.getString("title"));
                    note.setText(resultSet.getString("text"));
                    note.setCreateAt(resultSet.getObject("created_at", OffsetDateTime.class));
                    Author author = getAuthorById(resultSet.getInt("author_id"));
                    note.setAuthor(author);
                    return note;
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public Note getByAuthorId(int id){
        String selectSql = "SELECT id, title, text, created_at, author_id " +
                "FROM tb_notes " +
                "WHERE author_id = ?";

        try(Connection connection = C3P0pool.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(selectSql)){
                ps.setInt(1, id);
                ResultSet resultSet = ps.executeQuery();
                if(resultSet.next()){
                    Note note = new Note();
                    note.setId(resultSet.getLong("id"));
                    note.setTitle(resultSet.getString("title"));
                    note.setText(resultSet.getString("text"));
                    note.setCreateAt(resultSet.getObject("created_at", OffsetDateTime.class));
                    Author author = getAuthorById(resultSet.getInt("author_id"));

                    note.setAuthor(author);
                    return note;
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
}

//1. CHECK на created_at - дата в прошлом
// 2. INSERT tb_notes
// 3. SELECT tb_notes по идентификатору
// 4. SELECT tb_notes по идентификатору автора
// 5. SELECT tb_notes c LIMIT и OFFSET
