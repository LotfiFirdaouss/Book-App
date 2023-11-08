package com.lotfi.database.repositories;

import com.lotfi.database.TestDataUtil;
import com.lotfi.database.domain.entities.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // to clean the context after each test method , elsewise our test will sometimes be failing
public class AuthorRepositoryIntegrationTests {

    private AuthorRepository underTest;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatAuthorCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorA(); // creates an instance of author with values

        AuthorEntity createdAuthorEntity = underTest.save(authorEntity);
        Optional<AuthorEntity> result = underTest.findById(createdAuthorEntity.getId());

        // checking if exists and if it's equal to the author we provided
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(createdAuthorEntity);
    }

    @Test
    public void testThatMultipleAuthorsCanBeCreatedAndRecalled() {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorC();

        //underTest.saveAll(Arrays.asList(authorA, authorB, authorC));
        AuthorEntity createdAuthorAEntity = underTest.save(authorEntityA);
        System.out.println(createdAuthorAEntity);
        AuthorEntity createdAuthorBEntity = underTest.save(authorEntityB);
        AuthorEntity createdAuthorCEntity = underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).contains(createdAuthorBEntity, createdAuthorCEntity);
    }

    @Test
    public void testThatAuthorCanBeUpdated() {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        // create it on db
        underTest.save(authorEntityA);

        //change its values and update it
        authorEntityA.setName("New value");
        authorEntityA.setAge(100);
        underTest.save(authorEntityA);

        //check if updated
        Optional<AuthorEntity> updatedAuthor = underTest.findById(authorEntityA.getId());
        assertThat(updatedAuthor).isPresent();
        assertThat(updatedAuthor.get()).isEqualTo(authorEntityA);
    }

    @Test
    public void testThatAuthorCanBeDeleted() {
        // create AuthorA and delete it afterwards
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        underTest.save(authorEntityA);
        underTest.deleteById(authorEntityA.getId());

        //Check if it was properly deleted
        Optional<AuthorEntity> result = underTest.findById(authorEntityA.getId());
        assertThat(result).isEmpty();
    }

    @Test
    public void testThatGetAuthorsWithAgeLessThan() {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorC();
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.ageLessThan(50);
        assertThat(result).containsExactly(authorEntityB, authorEntityC);
    }

    @Test
    public void testThatGetAuthorsWithAgeGreaterThan() {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorA();
        underTest.save(authorEntityA);
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorB();
        underTest.save(authorEntityB);
        AuthorEntity authorEntityC = TestDataUtil.createTestAuthorC();
        underTest.save(authorEntityC);

        Iterable<AuthorEntity> result = underTest.findAuthorsWithAgeGreaterThan(50);
        assertThat(result).containsExactly(authorEntityA);
    }
}







