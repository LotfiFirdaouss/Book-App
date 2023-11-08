package com.lotfi.database;

import com.lotfi.database.domain.dto.AuthorDto;
import com.lotfi.database.domain.dto.BookDto;
import com.lotfi.database.domain.entities.AuthorEntity;
import com.lotfi.database.domain.entities.BookEntity;

public final class TestDataUtil {

    private TestDataUtil() {
    }


    public static AuthorEntity createTestAuthorA() {
        return AuthorEntity.builder()
                .id(1L)
                .name("abigail")
                .age(80)
                .build();
    }

    public static AuthorEntity createTestAuthorB() {
        return AuthorEntity.builder()
                .id(2L)
                .name("Thomas")
                .age(45)
                .build();
    }

    public static AuthorEntity createTestAuthorC() {
        return AuthorEntity.builder()
                .id(3L)
                .name("Jesse")
                .age(24)
                .build();
    }

    public static AuthorDto createTestAuthorDtoA() {
        return AuthorDto.builder()
                .id(1L)
                .name("abigail")
                .age(80)
                .build();
    }

    public static BookEntity createTestBookA(AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("123-832-78")
                .title("TITANIC")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookB(AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("456-832-78")
                .title("The Shadow in the Attic")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookEntity createTestBookC(AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("789-832-78")
                .title("Sense and Sensebility")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDto createTestBookDtoA(AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("123-832-78")
                .title("TITANIC")
                .author(authorDto)
                .build();
    }
}
