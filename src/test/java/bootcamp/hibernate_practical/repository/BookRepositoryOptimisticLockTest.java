
package bootcamp.hibernate_practical.repository;

import bootcamp.hibernate_practical.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryOptimisticLockTest {

    @Autowired
    private BookRepository repository;

    @Test
    void shouldPreventConcurrentBookUpdates() {
        Book book = new Book(
                "1984",
                "George Orwell",
                "Dystopian",
                1949,
                true
        );

        Book savedBook = repository.saveAndFlush(book);

        Book firstUser = repository.findById(savedBook.getId())
                .orElseThrow();

        Book secondUser = repository.findById(savedBook.getId())
                .orElseThrow();

        firstUser.setAvailable(false);
        repository.saveAndFlush(firstUser);

        secondUser.setAvailable(false);

        assertThrows(
                OptimisticLockingFailureException.class,
                () -> repository.saveAndFlush(secondUser)
        );

        Book latestBook = repository.findById(savedBook.getId())
                .orElseThrow();

        assertFalse(latestBook.isAvailable());
    }
}
