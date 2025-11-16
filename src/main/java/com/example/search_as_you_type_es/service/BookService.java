package com.example.search_as_you_type_es.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.search_as_you_type_es.model.Book;
import com.example.search_as_you_type_es.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;
@Slf4j
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
                .toList();
    }

    public List<Book> getAllBooks() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .toList();
    }

    public Book getBookById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> searchByTitle(String title) {
        try {
            Query query =
                Query.of(q -> q
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
                                .operator(Operator.And)
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

           NativeQuery searchQuery =
                org.springframework.data.elasticsearch.client.elc.NativeQuery.builder()
                    .withQuery(query)
                    .build();

            SearchHits<Book> searchHits =
                elasticsearchOperations.search(searchQuery, Book.class);

            return searchHits.stream()
                .map(SearchHit::getContent)
                    .toList();
        } catch (Exception e) {
            log.error("Error searching by title: {}", e.getMessage());
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
