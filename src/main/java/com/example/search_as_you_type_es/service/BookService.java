package com.example.search_as_you_type_es.service;

import com.example.search_as_you_type_es.model.Book;
import com.example.search_as_you_type_es.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public Book saveBook(Book book) {
        if (book.getSuggest() == null) {
            book.setSuggest(new Completion(new String[]{book.getTitle(), book.getAuthor(), book.getCategory()}));
        }
        return bookRepository.save(book);
    }

    public List<Book> saveAll(List<Book> books) {
        books.forEach(book -> {
            if (book.getSuggest() == null) {
                book.setSuggest(new Completion(new String[]{book.getTitle(), book.getAuthor(), book.getCategory()}));
            }
        });
        return (List<Book>) bookRepository.saveAll(books);
    }

    public List<Book> getAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book getBookById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> searchByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public List<Book> searchByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
