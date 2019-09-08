package pl.programator.cschool.bookshelf;

import fi.iki.elonen.NanoHTTPD.*;
import pl.programator.cschool.bookshelf.controller.BookController;

import static fi.iki.elonen.NanoHTTPD.Method.GET;
import static fi.iki.elonen.NanoHTTPD.Method.POST;
import static fi.iki.elonen.NanoHTTPD.Response.Status.NOT_FOUND;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class RequestUrlMapper {

    private final static String ADD_BOOK_URL = "/book/add";
    private final static String GET_BOOK_URL = "/book/get";
    private final static String GET_ALL_BOOK_URL = "/book/getAll";
    private BookController bookController = new BookController();

    public BookController getBookController() {
        return bookController;
    }



    public Response delegateRequest(IHTTPSession session) {


        if (GET.equals(session.getMethod()) && GET_BOOK_URL.equals(session.getUri())) {
            return bookController.serveGetBookRequest(session);

        } else if (GET.equals(session.getMethod()) && GET_ALL_BOOK_URL.equals(session.getUri())) {
            return bookController.serveGetAllBooksRequest(session);

        } else if (POST.equals(session.getMethod()) && ADD_BOOK_URL.equals(session.getUri())) {
            return bookController.serveAddBookRequest(session);
        }

        return newFixedLengthResponse(NOT_FOUND, "text/plain", "404! These are not the droids you're looking for...");
    }
}
