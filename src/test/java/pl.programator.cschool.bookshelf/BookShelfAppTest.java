package pl.programator.cschool.bookshelf;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class BookShelfAppTest {

    private static final String BOOK_1 = "{\"title\":\"Java. Kompendium programisty\",\"author\":" +
            "\"Herbert Schildt\",\"pagesSum\":1152, \"yearOfPublished\" : 2019, \"publishingHouse\":\"Helion\"}";

    private static final String BOOK_2 = "{\"title\":\"Python. Wprowadzenie.\",\"author\":\"Mark Lutz\"," +
            "\"pagesSum\":1184, \"yearOfPublished\" : 2017, \"publishingHouse\" :\"Helion\"}";

    private static final int APP_PORT = 8090; //inny port niz program, dla bezpieczenstwa, unikniecia konfliktow itp.

    private BookShelfApp bookShelfApp;

    @BeforeAll
    public static void beforeAll(){
        RestAssured.port = APP_PORT;
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        bookShelfApp = new BookShelfApp(APP_PORT);
    }

    @AfterEach
    public void afterEach() {
        bookShelfApp.stop();
    }

    @Test
    public void addMethod_correctBody_shouldReturnStatus200(){
        with().
                body(BOOK_1).
                when().
                post("/book/add").
                then().
                statusCode(200).
                body(equalTo("Book has been successfully added. The ID of the book is: 1"));
    }
}
