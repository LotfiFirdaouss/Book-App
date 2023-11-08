package com.lotfi.database.services.impl;

import com.lotfi.database.domain.entities.BookEntity;
import com.lotfi.database.repositories.BookRepository;
import com.lotfi.database.services.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity saveBook(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepository.save(bookEntity);
    }

    @Override
    public List<BookEntity> findAll() {
        return StreamSupport.stream(bookRepository
                .findAll()
                .spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        Optional<BookEntity> book = bookRepository.findById(isbn);
        return book;
    }

    @Override
    public boolean isExists(String isbn) {
        return bookRepository.existsById(isbn);
    }
}
