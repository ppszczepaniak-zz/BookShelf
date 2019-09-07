package pl.programator.cschool.bookshelf.storage.impl;

import pl.programator.cschool.bookshelf.storage.BookStorage;
import pl.programator.cschool.bookshelf.type.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PostgresBookStorageImpl implements BookStorage {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/databaseName"; //TODO databaseName
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASS = "password";
    private static Class psqlDriver;

    static {
        try {
            psqlDriver = Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Book getBook(long id) {
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        return null;
    }

    @Override
    public void addBook(Book book) {

    }

    private void makeConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, DATABASE_USER, DATABASE_PASS);
        Statement statement = connection.createStatement();
    }

    private void closeConnection(Statement statement, Connection connection) throws SQLException {
        statement.close();
        connection.close();
    }
}
