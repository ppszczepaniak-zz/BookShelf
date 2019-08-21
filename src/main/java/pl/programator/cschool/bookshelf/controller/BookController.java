package pl.programator.cschool.bookshelf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD.*;
import org.omg.CORBA.INTERNAL;
import pl.programator.cschool.bookshelf.storage.BookStorage;
import pl.programator.cschool.bookshelf.storage.impl.StaticListBookStorageImpl;

import static fi.iki.elonen.NanoHTTPD.Response.Status.INTERNAL_ERROR;
import static fi.iki.elonen.NanoHTTPD.Response.Status.OK;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;


public class BookController {
    private BookStorage bookStorage = new StaticListBookStorageImpl();

    public Response serveGetBookRequest(IHTTPSession session) {
        return null;
    }

    public Response serveGetAllBooksRequest(IHTTPSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        String response = "123";

        try {
            response = objectMapper.writeValueAsString(bookStorage.getAllBooks());

        } catch (JsonProcessingException e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error! Can't read all books...");
        }

        return newFixedLengthResponse(OK, "application/json", response);
    }

    public Response serveAddBookRequest(IHTTPSession session) {
        return null;
    }
}
