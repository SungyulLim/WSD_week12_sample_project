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

    // 페이지 번호, 크기, 정렬 조건을 받아 도서 목록과 페이지 정보를 반환한다.
    public Page<BookResponse> findAll(Pageable pageable) {
        // 전달받은 조건으로 DB를 조회하여 Page<Book> 결과를 얻는다.
        return repository.findAll(pageable)

                // 각 Book을 BookResponse로 변환한다.
                // 전체 데이터 수, 전체 페이지 수 등의 페이지 정보는 유지된다.
                .map(BookResponse::from);
    }

    public Book findById(Long id) {
        // ID로 도서를 조회한다. Repository의 반환값인 Optional<Book>은
        // 조회된 도서가 있거나, 조회 결과가 없는 상태를 표현한다.
        return repository.findById(id)
                // 값이 있으면 Book을 반환하고, 없으면 사용자 정의 예외를 발생시킨다.
                // 이 예외는 GlobalExceptionHandler에서 404 응답으로 처리된다.
                .orElseThrow(() -> new BookNotFoundException(id));
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
