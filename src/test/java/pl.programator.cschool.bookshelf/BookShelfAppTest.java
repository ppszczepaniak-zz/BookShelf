package pl.programator.cschool.bookshelf;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.programator.cschool.bookshelf.storage.impl.PostgresBookStorageImpl;
import pl.programator.cschool.bookshelf.storage.impl.StaticListBookStorageImpl;

import java.io.IOException;

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
        bookShelfApp.clear(); //czyszczenie
    }

    //Tests of ADD
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
        long bookId1 = addBookAndGetId(BOOK_1); //adds book 1, id = 1
        long bookId2 = addBookAndGetId(BOOK_2); //adds book 2, id = 2

        with().param("bookId", bookId1) //pobieram ksiazke z tego ID i powinna to byc własciwa
                .when().get("/book/get")
                .then().statusCode(200)
                .body("id", equalTo((int)bookId1)) //rzutuje na int bo sie krzaczy ten RestAssured tutaj (zwraca int, zamiast long)
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

    /*tests of GET
	1. istnieje książka dla podanego w parametrze “bookId”, oczekiwany status 200 i poprawny json z danymi książki
	2. brak parametru “bookId”, oczekiwany status 400 i tekst “Uncorrected request params”
	3. nieliczbowa wartość parametru “bookId”, oczekiwany status 400 i tekst “Request param 'bookId' have to be a number”
	4. brak książki o podanym id, oczekiwany status 404

     */
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

    /*tests of GET ALL
	1. Gdy w aplikacji nie ma żadnych zapisanych książek
	2. Gdy w aplikacji jest zapisana jedna książka
	3. Gdy w aplikacji są zapisane dwie książki

    */
    @Test
    public void getAllMethod_0books_shouldReturnStatus200() {
        when().get("/book/getAll").then().statusCode(200).body("", hasSize(0));
    }

    @Test
    public void getAllMethod_1book_shouldReturnStatus200() throws IOException {
        bookShelfApp.stop();
        bookShelfApp = new BookShelfApp(APP_PORT);

        long bookId1 = addBookAndGetId(BOOK_1); //adds book 1, id = 1

        when().get("/book/getAll")
                .then().statusCode(200)
                .body("", hasSize(1))
                .body("id", hasItem((int)bookId1))
                .body("title", hasItem("Java. Kompendium programisty"))
                .body("author", hasItem("Herbert Schildt"))
                .body("pagesSum", hasItem(1152))
                .body("yearOfPublished", hasItem(2019))
                .body("publishingHouse", hasItem("Helion"));
    }

    @Test
    public void getAllMethod_2books_shouldReturnStatus200() {
        long bookId1 = addBookAndGetId(BOOK_1); //adds book 1, id = 1
        long bookId2 = addBookAndGetId(BOOK_2); //adds book 2, id = 2

        when().get("/book/getAll")
                .then().statusCode(200)
                .body("", hasSize(2))
                .body("id", hasItems((int)bookId1, (int)bookId2))
                .body("title", hasItems("Java. Kompendium programisty", "Python. Wprowadzenie."))
                .body("author", hasItems("Herbert Schildt", "Mark Lutz"))
                .body("pagesSum", hasItems(1152, 1184))
                .body("yearOfPublished", hasItems(2019, 2017))
                .body("publishingHouse", hasItems("Helion", "Helion"));
    }
}


