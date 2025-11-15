package com.example.search_as_you_type_es.config;

import com.example.search_as_you_type_es.model.Book;
import com.example.search_as_you_type_es.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BookService bookService;

    @Override
    public void run(String... args) {
        // Clear existing data
        bookService.deleteAll();

        // Sample books
        List<Book> books = Arrays.asList(
            new Book("1", "The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", 
                "A classic American novel", "Fiction", 10.99),
            new Book("2", "To Kill a Mockingbird", "Harper Lee", "978-0061120084", 
                "A gripping tale of racial injustice", "Fiction", 12.99),
            new Book("3", "1984", "George Orwell", "978-0451524935", 
                "Dystopian social science fiction", "Science Fiction", 13.99),
            new Book("4", "Pride and Prejudice", "Jane Austen", "978-0141439518", 
                "A romantic novel of manners", "Romance", 9.99),
            new Book("5", "The Catcher in the Rye", "J.D. Salinger", "978-0316769174", 
                "A story about teenage rebellion", "Fiction", 11.99),
            new Book("6", "Harry Potter and the Sorcerer's Stone", "J.K. Rowling", "978-0439708180", 
                "A young wizard's journey begins", "Fantasy", 14.99),
            new Book("7", "The Hobbit", "J.R.R. Tolkien", "978-0547928227", 
                "An unexpected journey", "Fantasy", 15.99),
            new Book("8", "Fahrenheit 451", "Ray Bradbury", "978-1451673319", 
                "A dystopian novel about book burning", "Science Fiction", 12.49),
            new Book("9", "The Lord of the Rings", "J.R.R. Tolkien", "978-0544003415", 
                "Epic high fantasy adventure", "Fantasy", 25.99),
            new Book("10", "Animal Farm", "George Orwell", "978-0451526342", 
                "A satirical allegorical novella", "Fiction", 8.99)
        );

        bookService.saveAll(books);
        System.out.println("Sample books initialized successfully!");
    }
}
