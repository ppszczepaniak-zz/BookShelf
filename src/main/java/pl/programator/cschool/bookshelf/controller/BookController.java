package pl.programator.cschool.bookshelf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.bind.v2.TODO;
import fi.iki.elonen.NanoHTTPD.*;
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
        String response = "";

        try {
            response = objectMapper.writeValueAsString(bookStorage.getAllBooks());  //przypisuje liste ksiazek do Stringa w formacie JSON

        } catch (JsonProcessingException e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error! Can't read all books...");
        }

        return newFixedLengthResponse(OK, "application/json", response); //zwraca liste ksiazek w JSON
    }

    public Response serveAddBookRequest(IHTTPSession session) {
        //TODO finished here:
        //https://platform.cschool.pl/student-app/courses/277/modules/2845/lessons/3380/pages/7
        return null;
    }
}
