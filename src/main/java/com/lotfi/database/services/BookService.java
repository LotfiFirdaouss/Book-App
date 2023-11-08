package com.lotfi.database.services;

import com.lotfi.database.domain.entities.BookEntity;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BookEntity saveBook(String isbn, BookEntity bookEntity);

    List<BookEntity> findAll();

    Optional<BookEntity> findOne(String isbn);

    boolean isExists(String isbn);
}
