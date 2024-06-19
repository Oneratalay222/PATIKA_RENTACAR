package business;

import dao.BookDao;
import entity.Book;

import java.sql.SQLException;

public class BookManager {
    private final BookDao bookDao;

    public BookManager() throws SQLException {
        this.bookDao = new BookDao();
    }

    public boolean save(Book book) {
        return this.bookDao.save(book);
    }

}