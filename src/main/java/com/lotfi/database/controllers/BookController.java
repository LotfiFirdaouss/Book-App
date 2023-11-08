package com.lotfi.database.controllers;

import com.lotfi.database.domain.dto.BookDto;
import com.lotfi.database.domain.entities.BookEntity;
import com.lotfi.database.mappers.Mapper;
import com.lotfi.database.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private Mapper<BookEntity, BookDto> bookMapper;
    private BookService bookService;


    public BookController(Mapper<BookEntity, BookDto> bookMapper, BookService bookService) {
        this.bookMapper = bookMapper;
        this.bookService = bookService;
    }

    @PutMapping("/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);

        if(bookService.isExists(isbn)){ // checking if exists
            BookEntity savedBookEntity = bookService.saveBook(isbn,bookEntity); // <----------------
            BookDto savedBookDto = bookMapper.mapTo(savedBookEntity);
            return new ResponseEntity<>(savedBookDto, HttpStatus.ACCEPTED);
        }else{
            BookEntity savedBookEntity = bookService.saveBook(isbn,bookEntity); // <----------------
            BookDto savedBookDto = bookMapper.mapTo(savedBookEntity);
            return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
        }
    }

    @GetMapping("/books")
    public List<BookDto> findAll() {
        List<BookEntity> books = bookService.findAll();
        return books.stream().map(bookMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/books/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable("isbn") String isbn) {
        Optional<BookEntity> foundBook = bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDto book = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(book, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
