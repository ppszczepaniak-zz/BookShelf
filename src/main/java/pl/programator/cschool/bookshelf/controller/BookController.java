package pl.programator.cschool.bookshelf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD.*;
import pl.programator.cschool.bookshelf.storage.BookStorage;
import pl.programator.cschool.bookshelf.storage.impl.PostgresBookStorageImpl;
import pl.programator.cschool.bookshelf.storage.impl.StaticListBookStorageImpl;
import pl.programator.cschool.bookshelf.type.Book;

import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Response.Status.*;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class BookController {
     private BookStorage bookStorage = new StaticListBookStorageImpl(); //creates storage of books via JAVA
  //  private BookStorage bookStorage = new PostgresBookStorageImpl(); //creates storage of books via SQL

    private static final String BOOK_IT_PARAM_NAME = "bookId"; //used to get book from storage

    public Response serveGetBookRequest(IHTTPSession session) {
        Map<String, List<String>> requestParameters = session.getParameters(); //takes all params from session
        if (requestParameters.containsKey(BOOK_IT_PARAM_NAME)) { //if there is a parameter bookID, then...
            List<String> bookIdparams = requestParameters.get(BOOK_IT_PARAM_NAME); //gets list of parameters
            String bookIdparam = bookIdparams.get(0); //gets 1st
            long bookId = 0;

            try {
                bookId = Long.parseLong(bookIdparam);

            } catch (NumberFormatException nfe) { //if NaN
                System.err.println("Error during converting of request parameter: \n" + nfe);
                return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Request parameter 'bookID' must be a number!");
            }

            Book foundBook = bookStorage.getBook(bookId);

            if (foundBook != null) { //if this book we take isn't empty
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    String response = objectMapper.writeValueAsString(foundBook); //przypisuje ksiazke do Stringa w formacie JSON
                    return newFixedLengthResponse(OK, "application/json", response); //zwracam string w JSON

                } catch (JsonProcessingException e) {
                    System.err.println("Error during process request: \n" + e);
                    return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error! Can't read a book.");
                }

            } else {
                return newFixedLengthResponse(NOT_FOUND, "application/json", ""); //return when such book hasn't been found
            }

        } else {
            return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Incorrect request parameter!"); //return when there there's no bookID param in request
        }
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
        long bookId = 0;
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
            bookId = bookStorage.addBook(requestBook); //adds book to bookStorage and returns ID of the book
        } catch (Exception e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error! Book has not been added.");
        }

        return newFixedLengthResponse(OK, "text/plain", "Book has been successfully added. The ID of the book is: " + bookId); //zwraca liste ksiazek w JSON
    }
}
