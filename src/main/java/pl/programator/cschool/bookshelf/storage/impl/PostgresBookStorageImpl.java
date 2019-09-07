package pl.programator.cschool.bookshelf.storage.impl;

import pl.programator.cschool.bookshelf.storage.BookStorage;
import pl.programator.cschool.bookshelf.type.Book;

import java.sql.*;
import java.util.List;

public class PostgresBookStorageImpl implements BookStorage {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/bookshelf_db"; //create it first
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASS = "password";

    static {  //loading Driver class so it works on older Java or JDBC
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Server can't find postgresql Driver class:\n" + e);
        }
    }

    private Connection initialazeDataBaseConnection() { //otwiera polaczenie
        try {
            return DriverManager.getConnection(JDBC_URL, DATABASE_USER, DATABASE_PASS);
        } catch (SQLException e) {
            System.err.println("Server can't initialize database connection:\n" + e);
            throw new RuntimeException("Server can't initialize database connection"); //Runtime rzucony po to zeby tu przerwal dzialanie programu
        }
    }

    private void closeDatabaseResources(Statement statement, Connection connection) { //zamyka polaczenie i statement
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error during closing database resources:\n" + e);
            throw new RuntimeException("Error during closing database resources"); //Runtime rzucony po to zeby tu przerwal dzialanie programu
        }
    }


    @Override
    public void addBook(Book book) {
        final String sqlInsertBook = "INSER INTO books(" +
                "book_id, title, author, pages_sum, year_of_published, publishing_house)" +
                "VALUES (?,?,?,?,?,?);";

        Connection connection = initialazeDataBaseConnection(); //odpalamy połączenie
        PreparedStatement preparedStatement = null;

        try {  //przygotowuje Statement (prepared statement)
            preparedStatement = connection.prepareStatement(sqlInsertBook);

            preparedStatement.setLong(1, book.getId());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthor());
            preparedStatement.setInt(4, book.getPagesSum());
            preparedStatement.setInt(5, book.getYearOfPublished());
            preparedStatement.setString(6, book.getPublishingHouse());

            preparedStatement.executeUpdate(); //odpalam statement

        } catch (SQLException e) {
            System.err.println("Error during invoking SQL query:\n" + e.getMessage());
            throw new RuntimeException("Error during invoking SQL query");

        } finally { //zamykam statement i connection
            closeDatabaseResources(preparedStatement, connection);
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


}
