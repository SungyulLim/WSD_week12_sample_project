package edu.handong.week12.book;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Page<BookResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(BookResponse::from);
    }

    public Book findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book create(BookRequest request) {
        Book book = new Book(request.title(), request.author(), request.price());

        return repository.save(book);
    }

    public Book update(Long id, BookRequest request) {
        Book book = findById(id);

        book.update(request.title(), request.author(), request.price());

        return repository.save(book);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
