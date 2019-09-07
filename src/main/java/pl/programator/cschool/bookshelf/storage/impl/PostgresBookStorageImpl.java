package pl.programator.cschool.bookshelf.storage.impl;

import pl.programator.cschool.bookshelf.storage.BookStorage;
import pl.programator.cschool.bookshelf.type.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PostgresBookStorageImpl implements BookStorage {

    private static Class psqlDriver;

    static {
        try {
            psqlDriver = Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static String JDBC_URL = "jdbc:postgresql://localhost:5432/databaseName"; //TODO databaseName
    private static String DATABASE_USER = "postgres";
    private static String DATABASE_PASS = "password";




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
        /*
        my code here
         */

        statement.close();
        connection.close();
    }
}
