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

}
