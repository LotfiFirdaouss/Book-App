package com.lotfi.database.repositories;

import com.lotfi.database.TestDataUtil;
import com.lotfi.database.domain.entities.AuthorEntity;
import com.lotfi.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {

    private BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(BookRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatBookCanBeCreatesAndRecalled()  {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();
        BookEntity book = TestDataUtil.createTestBookA(authorEntity);

        underTest.save(book); // saves both the book and related author (since we have a cascade.all type)
        Optional<BookEntity> result = underTest.findById(book.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(book);
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA();

        BookEntity bookA = TestDataUtil.createTestBookA(authorEntity);
        BookEntity createdBookA = underTest.save(bookA);

        BookEntity bookB = TestDataUtil.createTestBookB(authorEntity);
        BookEntity createdBookB = underTest.save(bookB);

        BookEntity bookC = TestDataUtil.createTestBookC(authorEntity);
        BookEntity createdBookC = underTest.save(bookC);

        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(createdBookA,createdBookB,createdBookC);
    }

    @Test
    public void testThatBookCanBeUpdated() {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        BookEntity bookA = TestDataUtil.createTestBookA(authorEntityA);
        // create it on db
        underTest.save(bookA);

        //change its values and update it
        bookA.setTitle("New value");
        bookA.setAuthorEntity(authorEntityB);
        underTest.save(bookA);

        //check if updated
        Optional<BookEntity> updatedAuthor = underTest.findById(bookA.getIsbn());
        assertThat(updatedAuthor).isPresent();
        assertThat(updatedAuthor.get()).isEqualTo(bookA);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {
        // create AuthorA and delete it afterwards
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        BookEntity bookA = TestDataUtil.createTestBookA(authorEntityA);
        underTest.save(bookA);
        underTest.deleteById(bookA.getIsbn());

        //Check if it was properly deleted
        Optional<BookEntity> result = underTest.findById(bookA.getIsbn());
        assertThat(result).isEmpty();
    }

}
