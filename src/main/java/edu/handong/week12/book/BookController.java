package edu.handong.week12.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Book", description = "도서 관리 API")
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @Operation(summary = "도서 목록 조회", description = "등록된 도서를 페이지 단위로 조회")
    @GetMapping
    public ResponseEntity<Page<BookResponse>> list(@PageableDefault(size = 10) Pageable pageable) {
        Page<BookResponse> result = service.findAll(pageable);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "도서 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> get(@PathVariable Long id) {
        Book book = service.findById(id);
        BookResponse result = BookResponse.from(book);

        return ResponseEntity.ok(result);
    }

    // POST /api/books 요청을 처리한다.
    @PostMapping
    public ResponseEntity<BookResponse> create(
            // 요청 JSON을 BookRequest로 변환하고,
            // 메서드 실행 전에 DTO에 선언된 검증 조건을 검사한다.
            @Valid @RequestBody BookRequest request) {
        // 요청 데이터를 Service에 전달하고, DB에 저장된 Book Entity를 받는다.
        Book saved = service.create(request);

        // 저장된 Entity를 클라이언트에게 반환할 응답 DTO로 변환한다.
        BookResponse result = BookResponse.from(saved);

        // 등록 성공을 나타내는 201 상태 코드와 응답 DTO를 함께 반환한다.
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        Book updated = service.update(id, request);
        BookResponse result = BookResponse.from(updated);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}