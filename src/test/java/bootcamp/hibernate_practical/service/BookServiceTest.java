
package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.exception.BookNotFoundException;
import bootcamp.hibernate_practical.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepository repository;
    private BookService service;
    private Book book;

    @BeforeEach
    void setUp() {
        repository = mock(BookRepository.class);
        service = new BookService(repository);
        book = new Book("1984", "George Orwell", "Dystopian", 1949, true);
        book.setId(1L);
    }

    @Test
    void shouldCreateBook() {
        CreateBookRequest request = mock(CreateBookRequest.class);

        when(request.getTitle()).thenReturn("1984");
        when(request.getAuthor()).thenReturn("George Orwell");
        when(request.getGenre()).thenReturn("Dystopian");
        when(request.getPublicationYear()).thenReturn(1949);
        when(repository.save(any(Book.class))).thenReturn(book);

        BookResponse result = service.createBook(request);

        assertEquals("1984", result.getTitle());
        assertTrue(result.isAvailable());
        verify(repository).save(any(Book.class));
    }

    @Test
    void shouldGetAllBooks() {
        when(repository.findAll()).thenReturn(List.of(book));

        List<BookResponse> results = service.getAllBooks();

        assertEquals(1, results.size());
        assertEquals("1984", results.get(0).getTitle());
    }

    @Test
    void shouldGetBookById() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        BookResponse result = service.getBookById(1L);

        assertEquals(1L, result.getId());
        assertEquals("George Orwell", result.getAuthor());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> service.getBookById(99L)
        );
    }

    @Test
    void shouldUpdateBook() {
        UpdateBookRequest request = mock(UpdateBookRequest.class);
        when(request.getAvailable()).thenReturn(false);
        when(request.getTitle()).thenReturn("Updated 1984");
        when(request.getAuthor()).thenReturn("George Orwell");
        when(request.getGenre()).thenReturn("Fiction");
        when(request.getPublicationYear()).thenReturn(1949);
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(any(Book.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        BookResponse result = service.updateBook(1L, request);

        assertEquals("Updated 1984", result.getTitle());
        assertFalse(result.isAvailable());
    }

    @Test
    void shouldDeleteBook() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.deleteBook(1L);

        verify(repository).delete(book);
    }

    @Test
    void shouldSearchByTitle() {
        when(repository.findByTitleContainingIgnoreCase("1984"))
                .thenReturn(List.of(book));

        List<BookResponse> results = service.findByTitle("1984");

        assertEquals(1, results.size());
    }

    @Test
    void shouldSearchByPublicationYear() {
        when(repository.findByPublicationYearGreaterThan(1940))
                .thenReturn(List.of(book));

        List<BookResponse> results =
                service.findByPublicationYearAfter(1940);

        assertEquals(1, results.size());
    }

    @Test
    void shouldBorrowBook() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(any(Book.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        BookResponse result = service.borrowBook(1L);

        assertFalse(result.isAvailable());
    }

    @Test
    void shouldRejectDoubleBorrowing() {
        book.setAvailable(false);
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(
                IllegalStateException.class,
                () -> service.borrowBook(1L)
        );
    }

    @Test
    void shouldReturnBook() {
        book.setAvailable(false);
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(any(Book.class))).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        BookResponse result = service.returnBook(1L);

        assertTrue(result.isAvailable());
    }

    @Test
    void shouldCountBooks() {
        when(repository.count()).thenReturn(3L);

        assertEquals(3L, service.countBooks());
    }
}
