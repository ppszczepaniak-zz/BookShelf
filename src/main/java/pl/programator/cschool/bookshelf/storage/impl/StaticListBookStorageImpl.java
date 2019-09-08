package pl.programator.cschool.bookshelf.storage.impl;

import pl.programator.cschool.bookshelf.storage.BookStorage;
import pl.programator.cschool.bookshelf.type.Book;

import java.util.ArrayList;
import java.util.List;


public class StaticListBookStorageImpl implements BookStorage {

    private static List<Book> bookStorage = new ArrayList<>();

    @Override
    public Book getBook(long id) {
        for (Book book : bookStorage) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookStorage;
    }

    @Override
    public long addBook(Book book) {
        long id = 0;
        bookStorage.add(book);
        id = bookStorage.size();
        book.setId(id); //sets ID of the book
        return id;
    }
}
