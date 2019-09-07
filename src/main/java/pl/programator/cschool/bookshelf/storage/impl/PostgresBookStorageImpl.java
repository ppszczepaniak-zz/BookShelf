package pl.programator.cschool.bookshelf.storage.impl;

import pl.programator.cschool.bookshelf.storage.BookStorage;
import pl.programator.cschool.bookshelf.type.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresBookStorageImpl implements BookStorage {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/bookshelf_db"; //create it first
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASS = "password";
    private static List<Book> bookStorage = new ArrayList<>();

    static {  //loading Driver class so it works on older Java or JDBC (blok statyczny, odpali sie na starcie)
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
        final String sqlInsertBook = "INSERT INTO books(" +
                "book_id, title, author, pages_sum, year_of_published, publishing_house)" +
                "VALUES (NEXTVAL('sekwencja'),?,?,?,?,?);"; //dodaje NEXTVAL('sekwencja')

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
        final String sqlSelectAllBook = "SELECT * from books WHERE book_id = ?;";
        Connection connection = initialazeDataBaseConnection(); //odpalamy połączenie
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sqlSelectAllBook);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {  //jesli jest taka ksiazka w bazie to zwroc, else null (na koncu)
                Book book = new Book(); //tworze nowa ksiazke, jesli jest kolejny rekord w bazie

                book.setId(resultSet.getLong("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPagesSum(resultSet.getInt("pages_sum"));
                book.setYearOfPublished(resultSet.getInt("year_of_published"));
                book.setPublishingHouse(resultSet.getString("publishing_house"));

                return book;
            }
        } catch (SQLException e) {
            System.err.println("Error during invoking SQL query:\n" + e.getMessage());
            throw new RuntimeException("Error during invoking SQL query");
        } finally {//zamykam statement i connection
            closeDatabaseResources(preparedStatement, connection);
        }
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        bookStorage.clear(); //czyszcze liste, bo inaczej kazde wywolanie getAllBooks dopisywaloby wszystkie do listy
        final String sqlSelectAllBook = "SELECT * from books;";

        Connection connection = initialazeDataBaseConnection();
        Statement statement = null;

        try {
            statement = connection.createStatement(); //statement a nie preparedStatement bo proste zapytanie bez parametrow
            ResultSet resultSet = statement.executeQuery(sqlSelectAllBook); //odbieram z bazy wg zapytania (cala tabela books)

            while (resultSet.next()) {  //next idzie na kolejny wiersz i zwraca true, jeśli jest
                Book book = new Book(); //tworze nowa ksiazke, jesli jest kolejny rekord w bazie

                book.setId(resultSet.getLong("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setPagesSum(resultSet.getInt("pages_sum"));
                book.setYearOfPublished(resultSet.getInt("year_of_published"));
                book.setPublishingHouse(resultSet.getString("publishing_house"));

                bookStorage.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error during invoking SQL query:\n" + e.getMessage());
            throw new RuntimeException("Error during invoking SQL query");

        } finally {
            closeDatabaseResources(statement, connection);
        }
        return bookStorage;
    }
}
