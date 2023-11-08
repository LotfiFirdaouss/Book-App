package com.lotfi.database.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotfi.database.TestDataUtil;
import com.lotfi.database.domain.dto.AuthorDto;
import com.lotfi.database.domain.entities.AuthorEntity;
import com.lotfi.database.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {

    private AuthorService authorService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsHttp201Created() throws Exception {
        // Preparing our json object (our mock request body)
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJSON = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJSON)
        ).andExpect( // testing the mock post
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorSuccessfullyReturnsSavedAuthor() throws Exception {
        // Preparing our json object (our mock request body)
        AuthorEntity testAuthorA = TestDataUtil.createTestAuthorA();
        testAuthorA.setId(null);
        String authorJSON = objectMapper.writeValueAsString(testAuthorA);

        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJSON)
        ).andExpect( // testing the mock post
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("abigail")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(80)
        );
    }

    @Test
    public void testThatListAutorsReturnsHttpStatus200() throws Exception {
        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testThatListAutorsReturnsListOfAuthors() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorService.save(authorA);

        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect( // testing the mock post
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("abigail")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(80)
        );

    }

    @Test
    public void testThatListAutorByIdReturnsHttpStatus200WhenAuthorExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorService.save(authorA);

        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.get("/authors/"+authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()); // status 200
    }

    @Test
    public void testThatListAutorByIdReturnsHttpStatus404WhenNoAuthorExists() throws Exception {
        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.get("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()); // status 200
    }

    @Test
    public void testThatFindAuthorByIdReturnsAuthor() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorService.save(authorA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/"+authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorA.getAge())
        );
    }

    @Test
    public void testThatFullUpdateAuthorReturnsStatus202WhenExists() throws Exception {
        // 1 - Create the author
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorService.save(authorA);

        // 2 - Update the author
        authorA.setName("New name");
        authorA.setAge(0);
        String JsonAuthorA = objectMapper.writeValueAsString(authorA);
        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.put("/authors/"+authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonAuthorA)
        ).andExpect(MockMvcResultMatchers.status().isAccepted()); // status 200
    }

    @Test
    public void testThatFullUpdateAuthorReturns404WhenNotExists() throws Exception {
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorService.save(authorA);
        String JsonAuthorA = objectMapper.writeValueAsString(authorA);

        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.put("/authors/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonAuthorA)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()); // status 200
    }

    @Test
    public void testThatFullUpdateAuthorReturnsUpdatedAuthor() throws Exception {
        // 1 - Create the author
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        authorService.save(authorA);

        // 2 - Update the author
        authorA.setName("New name");
        authorA.setAge(0);
        String JsonAuthorA = objectMapper.writeValueAsString(authorA);
        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.put("/authors/"+ authorA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonAuthorA)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("New name")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(0)
        );

    }

    @Test
    public void testThatPartialUpdateAuthorReturns200_OkStatus() throws Exception {
        // 1 - Create the author
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorA);

        // 2 - Update the author -- only update the name
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        testAuthorDtoA.setName("UPDATED");
        String JsonAuthorA = objectMapper.writeValueAsString(testAuthorDtoA); // preparing the json object

        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.patch("/authors/"+ savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonAuthorA)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateAuthorReturnsUpdatedAuthor() throws Exception {
        // 1 - Create the author
        AuthorEntity authorA = TestDataUtil.createTestAuthorA();
        AuthorEntity savedAuthor = authorService.save(authorA);

        // 2 - Update the author -- only update the name
        AuthorDto testAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        testAuthorDtoA.setName("UPDATED");
        String JsonAuthorA = objectMapper.writeValueAsString(testAuthorDtoA); // preparing the json object

        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.patch("/authors/"+ savedAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonAuthorA)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        );
    }










}
