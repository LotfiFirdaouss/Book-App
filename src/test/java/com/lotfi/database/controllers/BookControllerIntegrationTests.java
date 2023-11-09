package com.lotfi.database.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotfi.database.TestDataUtil;
import com.lotfi.database.domain.dto.BookDto;
import com.lotfi.database.domain.entities.BookEntity;
import com.lotfi.database.services.BookService;
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
public class BookControllerIntegrationTests {

    private BookService bookService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTests(MockMvc mockMvc,BookService bookService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.bookService = bookService;
    }

    @Test
    public void testThatCreateBookReturnsHttpStatus201Created() throws Exception {
        BookDto testBookDto = TestDataUtil.createTestBookDtoA(null);
        String bookJSON = objectMapper.writeValueAsString(testBookDto);

        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.put("/books/"+ testBookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJSON)
        ).andExpect( // testing the mock post
                MockMvcResultMatchers.status().isCreated()
        );

    }

    @Test
    public void testThatCreateBookReturnsCreatedBook() throws Exception {
        BookDto testBookDto = TestDataUtil.createTestBookDtoA(null);
        String bookJSON = objectMapper.writeValueAsString(testBookDto);

        mockMvc.perform( // performing a mock post
                MockMvcRequestBuilders.put("/books/"+ testBookDto.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJSON)
        ).andExpect( // testing the mock post
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookDto.getTitle())
        );
    }

    @Test
    public void testThatListBooksReturns200Status() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksReturnsListOfBook() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(TestDataUtil.createTestAuthorA());
        bookService.createUpdateBook(testBookA.getIsbn(),testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect( // testing the mock post
                    MockMvcResultMatchers.jsonPath("$[0].isbn").value(testBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").value(testBookA.getTitle())
        );
    }

    @Test
    public void testThatFindBookReturnsStatus200WhenBookExists() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(TestDataUtil.createTestAuthorA());
        bookService.createUpdateBook(testBookA.getIsbn(),testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+ testBookA.getIsbn()).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFindBookReturnsStatus200WhenBookNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/978-0-13-378627-2").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFindBookReturnsFoundBook() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(TestDataUtil.createTestAuthorA());
        bookService.createUpdateBook(testBookA.getIsbn(),testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/"+ testBookA.getIsbn()).contentType(MediaType.APPLICATION_JSON)
        ).andExpect( // testing the mock post
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookA.getTitle())
        );
    }

    @Test
    public void testThatCreateUpdateBookReturns201CreatedStatusIfNotExists() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(TestDataUtil.createTestAuthorA());
        String JsonTestBookA = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+ testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestBookA)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateUpdateBookCreatesBookIfNotExists() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(TestDataUtil.createTestAuthorA());
        String JsonTestBookA = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+ testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestBookA)
        ).andExpect( // testing the mock post
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookA.getTitle())
        );
    }

    @Test
    public void testThatCreateUpdateBookReturns202AcceptedStatusIfExists() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(TestDataUtil.createTestAuthorA());
        bookService.createUpdateBook(testBookA.getIsbn(),testBookA);
        testBookA.setTitle("NEW TITLE");

        String JsonTestBookA = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+ testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestBookA)
        ).andExpect(
                MockMvcResultMatchers.status().isAccepted()
        );
    }

    @Test
    public void testThatCreateUpdateBookReturnsUpdatedBookIfExists() throws Exception {
        BookEntity testBookA = TestDataUtil.createTestBookA(TestDataUtil.createTestAuthorA());
        bookService.createUpdateBook(testBookA.getIsbn(),testBookA);
        testBookA.setTitle("NEW TITLE");

        String JsonTestBookA = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/"+ testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestBookA)
        ).andExpect( // testing the mock post
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookA.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("NEW TITLE")
        );
    }

    @Test
    public void testThatPartialUpdateBookReturns200okStatus() throws Exception{
        BookEntity testBookA = TestDataUtil.createTestBookA(TestDataUtil.createTestAuthorA());
        bookService.createUpdateBook(testBookA.getIsbn(),testBookA);
        testBookA.setTitle("NEW TITLE");

        String JsonTestBookA = objectMapper.writeValueAsString(testBookA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+ testBookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonTestBookA)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateBookReturnsUpdatedBook() throws Exception{
        BookEntity testBookEntity = TestDataUtil.createTestBookA(TestDataUtil.createTestAuthorA());
        BookEntity savedBookEntity = bookService.createUpdateBook(testBookEntity.getIsbn(), testBookEntity);

        savedBookEntity.setTitle("NEW TITLE");
        String bookJSON = objectMapper.writeValueAsString(savedBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/"+ savedBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(savedBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("NEW TITLE")
        );
    }

    @Test
    public void testThatDeleteBookReturns204ifExists() throws Exception{
        BookEntity testBookEntity = TestDataUtil.createTestBookA(TestDataUtil.createTestAuthorA());
        BookEntity savedBookEntity = bookService.createUpdateBook(testBookEntity.getIsbn(), testBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + savedBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteBookReturns404ifNotExists() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/1000")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

}
