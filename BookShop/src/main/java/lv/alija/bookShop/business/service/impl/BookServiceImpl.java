package lv.alija.bookShop.business.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lv.alija.bookShop.business.mapper.BookMapper;
import lv.alija.bookShop.business.repository.BookRepository;
import lv.alija.bookShop.business.repository.model.BookDAO;
import lv.alija.bookShop.business.service.BookService;
import lv.alija.bookShop.model.Book;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Cacheable(value = "bookList")
    @Scheduled(fixedDelay = 300000)
    @Override
    public List<Book> findAllBooks() {
        List<BookDAO> booksDAO = bookRepository.findAll();
        log.info("Get book list. Size is : {}", booksDAO::size);
        return booksDAO.stream().map(bookMapper::bookDAOToBook)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthor(String author){
        List<BookDAO> bookDAOList = bookRepository.findByAuthor(author);
        return bookDAOList.stream().map(bookMapper::bookDAOToBook)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        Optional<Book> bookById = bookRepository.findById(id)
                .flatMap(bookDAO -> Optional.ofNullable(bookMapper.bookDAOToBook(bookDAO)));
        log.info("Book with id {} is {}", id, bookById);
        return bookById;
    }

    @CacheEvict(cacheNames = "bookList", allEntries = true)
    @Override
    public Book saveBook(Book book) throws Exception {
        if (!hasNoMatch(book)) {
            log.error("Book with same isbn number already exists. Conflict exception. ");
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        BookDAO bookDAO = bookMapper.bookToBookDAO(book);
        BookDAO bookSaved = bookRepository.save(bookDAO);
        log.info("New book saved: {}", () -> bookSaved);
        return bookMapper.bookDAOToBook(bookSaved);
    }

    @CacheEvict(cacheNames = "bookList", allEntries = true)
    @Override
    public Book updateBook(Book book) throws Exception {
        BookDAO bookDAO = bookMapper.bookToBookDAO(book);
        BookDAO bookSaved = bookRepository.save(bookDAO);
        log.info("Book is updated: {}", () -> bookSaved);
        return bookMapper.bookDAOToBook(bookSaved);
    }

    @CacheEvict(cacheNames = "bookList", allEntries = true)
    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
        log.info("Book with id {} is deleted", id);
    }

    public boolean hasNoMatch(Book book) {
        return bookRepository.findAll().stream()
                .noneMatch(newBook ->
                        newBook.getIsbn().equalsIgnoreCase(book.getIsbn())
                );
    }
}
