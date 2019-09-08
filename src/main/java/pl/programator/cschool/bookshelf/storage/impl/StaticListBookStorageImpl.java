package pl.programator.cschool.bookshelf.storage.impl;

import pl.programator.cschool.bookshelf.storage.BookStorage;
import pl.programator.cschool.bookshelf.type.Book;

import java.util.ArrayList;
import java.util.List;


public class StaticListBookStorageImpl implements BookStorage {

    public static List<Book> getBookList() {
        return bookList;
    }

    private static List<Book> bookList = new ArrayList<>();

    @Override
    public Book getBook(long id) {
        for (Book book : bookList) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookList;
    }

    @Override
    public long addBook(Book book) {
        long id = 0;
        bookList.add(book);
        id = bookList.size();
        book.setId(id); //sets ID of the book
        return id;
    }

    @Override
    public void clearDatabase() {
       bookList.clear();
    }


}
