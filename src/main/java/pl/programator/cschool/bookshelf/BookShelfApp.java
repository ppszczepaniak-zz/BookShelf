package pl.programator.cschool.bookshelf;

import fi.iki.elonen.NanoHTTPD;

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
     * 1) in BookController.java choose (comment out) which implementation to use
     * StaticListBookStorageImpl (Java List) or PostgresBookStorageImpl (SQL)     * 1) in BookController.java choose (comment out) which implementation to use
     * StaticListBookStorageImpl (Java List) or PostgresBookStorageImpl (SQL)
     *
     * 2) Fire up in Postman:
     * POST http://localhost:8080/book/add +in BODY: fill some JSON, see bookExample.txt
     * GET http://localhost:8080/book/getAll
     * GET http://localhost:8080/book/get +in PARAMS: add bookId, value: [number - first add book, and find one ID by getAll]
     */
}
