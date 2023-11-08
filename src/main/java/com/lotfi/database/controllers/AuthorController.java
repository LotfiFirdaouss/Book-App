package com.lotfi.database.controllers;

import com.lotfi.database.domain.dto.AuthorDto;
import com.lotfi.database.domain.entities.AuthorEntity;
import com.lotfi.database.mappers.Mapper;
import com.lotfi.database.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private AuthorService authorService;
    private Mapper<AuthorEntity, AuthorDto> authorMapper;

    public AuthorController(AuthorService authorService, Mapper<AuthorEntity, AuthorDto> authorMapper){
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto author) {
        AuthorEntity  authorEntity = authorMapper.mapFrom(author);
        AuthorEntity savedAuthorEntity = authorService.save(authorEntity);
        return new ResponseEntity<>(authorMapper.mapTo(savedAuthorEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/authors")
    public List<AuthorDto> listAuthors() {
        List<AuthorEntity> authors = authorService.findAll();
        return authors.stream().map(authorMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping(path = "/authors/{authorId}")
    public ResponseEntity<AuthorDto> findAuthor(@PathVariable("authorId") Long authorId){
        Optional<AuthorEntity> author = authorService.findById(authorId);
        return author.map(authorEntity -> {
            AuthorDto authorDto = authorMapper.mapTo(authorEntity);
            return new ResponseEntity<>(authorDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "authors/{authorId}")
    public ResponseEntity<AuthorDto> fullUpdateAuthor(@PathVariable("authorId") Long authorId, @RequestBody AuthorDto newAuthor) {
        if(!authorService.isExists(authorId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newAuthor.setId(authorId);
        AuthorEntity updatedAuthor = authorService.save(authorMapper.mapFrom(newAuthor));
        return new ResponseEntity<>(authorMapper.mapTo(updatedAuthor), HttpStatus.ACCEPTED);
    }

    @PatchMapping(path = "authors/{authorId}")
    public ResponseEntity<AuthorDto> partialUpdateAuthor(@PathVariable("authorId") Long id, @RequestBody AuthorDto author) {
        if(!authorService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AuthorEntity updatedAuthor = authorService.partialUpdate(id,authorMapper.mapFrom(author));
        return new ResponseEntity<>(authorMapper.mapTo(updatedAuthor), HttpStatus.OK);
    }








}
