package pl.programator.cschool.bookshelf.controller;

import fi.iki.elonen.NanoHTTPD;
import pl.programator.cschool.bookshelf.storage.BookStorage;
import pl.programator.cschool.bookshelf.storage.impl.StaticListBookStorageImpl;

public class BookController {
    private BookStorage bookStorage = new StaticListBookStorageImpl();

    public NanoHTTPD.Response serveGetBookRequest(NanoHTTPD.IHTTPSession session) {
        return null;
    }

    public NanoHTTPD.Response serveGetBooksRequest(NanoHTTPD.IHTTPSession session) {
        return null;
    }

    public NanoHTTPD.Response serveAddBookRequest(NanoHTTPD.IHTTPSession session) {
        return null;
    }
}
