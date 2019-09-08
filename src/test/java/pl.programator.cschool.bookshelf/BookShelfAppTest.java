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
            "\"Herbert Schildt\",\"pagesSum\":1152, \"yearOfPublished\":2019, \"publishingHouse\":\"Helion\"}";

    private static final String BOOK_2 = "{\"title\":\"Python. Wprowadzenie.\",\"author\":\"Mark Lutz\"," +
            "\"pagesSum\":1184, \"yearOfPublished\":2017, \"publishingHouse\" :\"Helion\"}";

    private static final int APP_PORT = 8090; //inny port niz program, dla bezpieczenstwa, unikniecia konfliktow itp.

    private BookShelfApp bookShelfApp;

    @BeforeAll
    public static void beforeAll() {
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
    public void addMethod_correctBody_shouldReturnStatus200() {
        with().
                body(BOOK_1).
                when().
                post("/book/add").
                then().
                statusCode(200).
                body(startsWith("Book has been successfully added. The ID of the book is: "));
    }

    @Test
    public void addMethod_fieldTypeMismatch_shouldReturnStatus500() {

        String bookWithFieldTypeMismatch = "{\"title\":\"Python. Wprowadzenie.\",\"author\":\"Mark Lutz\"," +
                "\"pagesSum\":\"1184 pages\", \"yearOfPublished\" : 2017, \"publishingHouse\" :\"Helion\"}";
        with().
                body(bookWithFieldTypeMismatch).
                when().
                post("/book/add").
                then().
                statusCode(500);
    }

    @Test
    public void addMethod_unexpectedField_shouldReturnStatus500() {
        with().
                body("{\"numberOfChapters\":10}").
                when().
                post("/book/add").
                then().
                statusCode(500);
    }

    private long addBookAndGetId(String json) { //gets ID of the book
        String responseText = with().body(json)
                .when().post("/book/add")
                .then().statusCode(200).body(startsWith("Book has been successfully added. The ID of the book is: "))
                .extract().body().asString();
        String idStr = responseText.substring(responseText.indexOf(":") + 2); //zapisuje ID do Stringa
        return Long.parseLong(idStr);
    }

    @Test
    public void getMethod_correctBookIdParam_shouldReturnStatus200() {
        int bookId1 = (int) addBookAndGetId(BOOK_1); //adds book 1, id = 1
        /** dlaczego musze castowac do int?  JSON wychodzi int, mimo ze powinien byc long*/


        long bookId2 = (int) addBookAndGetId(BOOK_2); //adds book 2, id = 2

        with().param("bookId", bookId1) //pobieram ksiazke z tego ID i powinna to byc własciwa
                .when().get("/book/get")
                .then().statusCode(200)
                .body("id", equalTo(bookId1))
                .body("title", equalTo("Java. Kompendium programisty"))
                .body("author", equalTo("Herbert Schildt"))
                .body("pagesSum", equalTo(1152))
                .body("yearOfPublished", equalTo(2019))
                .body("publishingHouse", equalTo("Helion"));
    }


    @Test
    public void getMethod_noBookIdParam_shouldReturnStatus400() {
        when()
                .get("book/get")
                .then().statusCode(400)
                .body(equalTo("Incorrect request parameter!"));
    }


    @Test
    public void getMethod_wrongTypeBookIdParam_shouldReturnStatus400() {
        with().param("bookId", "abc") //bledny ID NaN
                .when().get("/book/get")
                .then().statusCode(400)
                .body(equalTo("Request parameter 'bookID' must be a number!"));
    }

    @Test
    public void getMethod_bookDoesntExist_shouldReturnStatus404() {
        with().param("bookId", 1243452) //bledny ID
                .when().get("/book/get")
                .then().statusCode(404);
    }

}
