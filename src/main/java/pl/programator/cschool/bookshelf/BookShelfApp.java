package pl.programator.cschool.bookshelf;

import com.sun.org.apache.regexp.internal.RE;
import fi.iki.elonen.NanoHTTPD;
import pl.programator.cschool.bookshelf.controller.BookController;

import java.io.IOException;

public class BookShelfApp extends NanoHTTPD {

    RequestUrlMapper requestUrlMapper = new RequestUrlMapper();

    public void clear() {
       requestUrlMapper.getBookController().getBookStorage().clearDatabase();
    }

    public BookShelfApp(int port) throws IOException {
        super(port);
        start(5000, false); //daemon: should server start as a separate thread?
        System.out.println("I've started your server...");
    }

    public static void main(String[] args) {

        try {
            new BookShelfApp(8080);
        } catch (IOException e) {
            System.err.println("Can't start your server! Error: \n" + e);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        return requestUrlMapper.delegateRequest(session);
    }



    /** HOW IT WORKS
     * w BookController decydujesz komentarzem ktorej implementacji uzyc:
     * StaticListBookStorageImpl czy PostgresBookStorageImpl
     *
     * Fire up in Postman:
     * POST http://localhost:8080/book/add +in BODY: fill some JSON, see bookExample.txt
     * GET http://localhost:8080/book/getAll
     * GET http://localhost:8080/book/get +in PARAMS: add bookId, value: [number - first add book, and find one ID by getAll]
     */
}
