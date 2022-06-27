package lv.alija.bookShop.business.mapper;

import javax.annotation.Generated;
import lv.alija.bookShop.business.repository.model.BookDAO;
import lv.alija.bookShop.model.Book;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-06-27T12:45:12+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_321 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDAO bookToBookDAO(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDAO bookDAO = new BookDAO();

        bookDAO.setId( book.getId() );
        bookDAO.setTitle( book.getTitle() );
        bookDAO.setAuthor( book.getAuthor() );
        bookDAO.setGenre( book.getGenre() );
        bookDAO.setReleaseYear( book.getReleaseYear() );
        bookDAO.setIsbn( book.getIsbn() );
        bookDAO.setQuantity( book.getQuantity() );
        bookDAO.setPrice( book.getPrice() );

        return bookDAO;
    }

    @Override
    public Book bookDAOToBook(BookDAO bookDAO) {
        if ( bookDAO == null ) {
            return null;
        }

        Book book = new Book();

        book.setId( bookDAO.getId() );
        book.setTitle( bookDAO.getTitle() );
        book.setAuthor( bookDAO.getAuthor() );
        book.setGenre( bookDAO.getGenre() );
        book.setReleaseYear( bookDAO.getReleaseYear() );
        book.setIsbn( bookDAO.getIsbn() );
        book.setQuantity( bookDAO.getQuantity() );
        book.setPrice( bookDAO.getPrice() );

        return book;
    }
}
