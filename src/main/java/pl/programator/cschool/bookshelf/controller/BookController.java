package pl.programator.cschool.bookshelf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD.*;
import pl.programator.cschool.bookshelf.storage.BookStorage;
import pl.programator.cschool.bookshelf.storage.impl.StaticListBookStorageImpl;
import pl.programator.cschool.bookshelf.type.Book;

import java.io.IOException;

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
        String response;

        try {
            response = objectMapper.writeValueAsString(bookStorage.getAllBooks());  //przypisuje liste ksiazek do Stringa w formacie JSON

        } catch (JsonProcessingException e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error! Can't get all books.");
        }

        return newFixedLengthResponse(OK, "application/json", response); //zwraca liste ksiazek w JSON
    }

    public Response serveAddBookRequest(IHTTPSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        long randomBookID = System.currentTimeMillis(); //random ID of the book (will be added automatically)

        String lengthHeader = session.getHeaders().get("content-length"); //reads content length (in bytes) from headers (indicates the size of the entity-body)
        int contentLength = Integer.parseInt(lengthHeader); //converts to int
        byte[] buffer = new byte[contentLength]; //creates table of that many bytes (buffer)

        try {
            session.getInputStream().read(buffer, 0, contentLength);    /* reads "body" of the request -> read(buffer, offset, length)
                                                                             buffer - data will be put into this
                                                                             offset - starting point of reading (skip something?)
                                                                             length - how much do we read */
            String requestBody = new String(buffer).trim();       //creates string from updated buffer data, trims whitespaces
            Book requestBook = objectMapper.readValue(requestBody, Book.class); /*creates book from JSON data ->readValue(content,value type)
                                                                                content - String in JSON, used to create object
                                                                                value type - we point Java class, of which object should be created*/
            requestBook.setId(randomBookID); //adds ID to the new book
            bookStorage.addBook(requestBook); //adds book to bookStorage

        } catch (Exception e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error! Book has not been added.");
        }

        return newFixedLengthResponse(OK, "text/plain", "Book has been successfully added. The ID of the book is: " + randomBookID); //zwraca liste ksiazek w JSON
    }
}
