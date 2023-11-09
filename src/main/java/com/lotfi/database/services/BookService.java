package com.lotfi.database.services;

import com.lotfi.database.domain.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BookEntity createUpdateBook(String isbn, BookEntity bookEntity);

    List<BookEntity> findAll();
    Page<BookEntity> findAll(Pageable pageable);


    Optional<BookEntity> findOne(String isbn);

    boolean isExists(String isbn);

    BookEntity partialUpdateBook(String isbn, BookEntity bookEntity);

    void delete(String isbn);
}
