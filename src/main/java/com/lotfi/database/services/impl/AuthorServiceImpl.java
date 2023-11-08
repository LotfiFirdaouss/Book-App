package com.lotfi.database.services.impl;

import com.lotfi.database.domain.entities.AuthorEntity;
import com.lotfi.database.repositories.AuthorRepository;
import com.lotfi.database.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    @Override
    public List<AuthorEntity> findAll() {
        return StreamSupport.stream(authorRepository
                    .findAll()
                    .spliterator(),
                    false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorEntity> findById(Long authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    public boolean isExists(Long authorId) {
        return authorRepository.findById(authorId).isPresent();
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity) {
        authorEntity.setId(id);
        return authorRepository.findById(id).map(existingAuthor -> {
            Optional.ofNullable(authorEntity.getName()).ifPresent(existingAuthor::setName);
            Optional.ofNullable(authorEntity.getAge()).ifPresent(existingAuthor::setAge);
            return authorRepository.save(existingAuthor);
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }
}
