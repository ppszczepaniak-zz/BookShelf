package pl.programator.cschool.bookshelf.storage;

import pl.programator.cschool.bookshelf.type.Book;

import java.util.List;

public interface BookStorage2 {  //TEMP interfejs, ktory nie zmienia
    Book getBook(long id);

    List<Book> getAllBooks();

    int addBook(Book book);
}
