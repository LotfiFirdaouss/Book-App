package com.lotfi.database.controllers;

import com.lotfi.database.domain.dto.BookDto;
import com.lotfi.database.domain.entities.BookEntity;
import com.lotfi.database.mappers.Mapper;
import com.lotfi.database.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            BookEntity savedBookEntity = bookService.createUpdateBook(isbn,bookEntity); // <----------------
            BookDto savedBookDto = bookMapper.mapTo(savedBookEntity);
            return new ResponseEntity<>(savedBookDto, HttpStatus.ACCEPTED);
        }else{
            BookEntity savedBookEntity = bookService.createUpdateBook(isbn,bookEntity); // <----------------
            BookDto savedBookDto = bookMapper.mapTo(savedBookEntity);
            return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
        }
    }

    @GetMapping("/books")
    public Page<BookDto> findAll(Pageable pageable) {
        //List<BookEntity> books = bookService.findAll();
        //return books.stream().map(bookMapper::mapTo).collect(Collectors.toList());
        Page<BookEntity> books = bookService.findAll(pageable);
        return books.map(bookMapper::mapTo);
    }

    @GetMapping("/books/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable("isbn") String isbn) {
        Optional<BookEntity> foundBook = bookService.findOne(isbn);
        return foundBook.map(bookEntity -> {
            BookDto book = bookMapper.mapTo(bookEntity);
            return new ResponseEntity<>(book, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);

        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BookEntity savedBook = bookService.partialUpdateBook(isbn, bookEntity);
        BookDto savedBookDto = bookMapper.mapTo(savedBook);

        return new ResponseEntity<>(savedBookDto, HttpStatus.OK);
    }

    @DeleteMapping("/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn) {
        if(!bookService.isExists(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookService.delete(isbn);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
