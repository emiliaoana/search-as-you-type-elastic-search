package com.example.search_as_you_type_es.controller;

import com.example.search_as_you_type_es.model.Book;
import com.example.search_as_you_type_es.service.BookService;
import com.example.search_as_you_type_es.service.SuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final SuggestionService suggestionService;

    @Operation(summary = "Create a book", description = "Creates a new Book")
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.saveBook(book));
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves all books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves a book by its ID")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        Book book = bookService.getBookById(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @GetMapping("/search/title")
    @Operation(summary = "Search books by title", description = "Searches for books by their title")
    public ResponseEntity<List<Book>> searchByTitle(@RequestParam String query) {
        return ResponseEntity.ok(bookService.searchByTitle(query));
    }

    @Operation(summary = "Search books by author", description = "Searches for books by their author")
    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchByAuthor(@RequestParam String author) {
        return ResponseEntity.ok(bookService.searchByAuthor(author));
    }

    @GetMapping("/search/category")
    @Operation(summary = "Search books by category", description = "Searches for books by their category")
    public ResponseEntity<List<Book>> searchByCategory(@RequestParam String category) {
        return ResponseEntity.ok(bookService.searchByCategory(category));
    }

    @GetMapping("/suggest")
    @Operation(summary = "Get search suggestions", description = "Provides search-as-you-type suggestions")
    public ResponseEntity<List<Book>> getSuggestions(@RequestParam String query) {
        return ResponseEntity.ok(suggestionService.searchAsYouType(query));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by ID", description = "Deletes a book by its ID")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete all books", description = "Deletes all books from the index")
    public ResponseEntity<Void> deleteAllBooks() {
        bookService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
