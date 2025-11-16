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
import java.util.stream.StreamSupport;

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
        return StreamSupport.stream(bookRepository.saveAll(books).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    public Book getBookById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> searchByTitle(String title) {
        try {
            co.elastic.clients.elasticsearch._types.query_dsl.Query query =
                co.elastic.clients.elasticsearch._types.query_dsl.Query.of(q -> q
                    .bool(b -> b
                        .should(s -> s
                            .matchPhrase(m -> m
                                .field("title")
                                .query(title)
                                .boost(5.0f)
                            )
                        )
                        .should(s -> s
                            .match(m -> m
                                .field("title")
                                .query(title)
                                .operator(co.elastic.clients.elasticsearch._types.query_dsl.Operator.And)
                                .boost(3.0f)
                            )
                        )
                        .should(s -> s
                            .matchPhrase(m -> m
                                .field("author")
                                .query(title)
                                .boost(2.0f)
                            )
                        )
                        .minimumShouldMatch("1")
                    )
                );

            org.springframework.data.elasticsearch.client.elc.NativeQuery searchQuery = 
                org.springframework.data.elasticsearch.client.elc.NativeQuery.builder()
                    .withQuery(query)
                    .build();

            org.springframework.data.elasticsearch.core.SearchHits<Book> searchHits = 
                elasticsearchOperations.search(searchQuery, Book.class);

            return searchHits.stream()
                .map(org.springframework.data.elasticsearch.core.SearchHit::getContent)
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error searching by title: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
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
