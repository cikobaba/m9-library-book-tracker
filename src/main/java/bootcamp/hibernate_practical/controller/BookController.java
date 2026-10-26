package bootcamp.hibernate_practical.controller;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest request) {
        return bookService.createBook(request);
    }

    @GetMapping
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BookResponse updateBook(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookRequest request) {
        return bookService.updateBook(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/author/{author}")
    public List<BookResponse> getBooksByAuthor(@PathVariable String author) {
        return bookService.findByAuthor(author);
    }

    @GetMapping("/available")
    public List<BookResponse> getAvailableBooks() {
        return bookService.findAvailableBooks();
    }

    @GetMapping("/search")
    public List<BookResponse> searchByTitle(@RequestParam String title) {
        return bookService.findByTitle(title);
    }

    @GetMapping("/published-after/{year}")
    public List<BookResponse> getBooksPublishedAfter(@PathVariable int year) {
        return bookService.findByPublicationYearAfter(year);
    }

    @GetMapping("/count")
    public long countBooks() {
        return bookService.countBooks();
    }

    @PostMapping("/{id}/borrow")
    public BookResponse borrowBook(@PathVariable Long id) {
        return bookService.borrowBook(id);
    }

    @PostMapping("/{id}/return")
    public BookResponse returnBook(@PathVariable Long id) {
        return bookService.returnBook(id);
    }

}
