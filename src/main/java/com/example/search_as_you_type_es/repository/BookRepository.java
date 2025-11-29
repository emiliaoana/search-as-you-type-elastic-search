package com.example.search_as_you_type_es.repository;

import com.example.search_as_you_type_es.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookRepository extends ElasticsearchRepository<Book, String> {
    List<Book> findByAuthor(String author);
    List<Book> findByCategory(String category);
    List<Book> findByTitle(String title);
}
