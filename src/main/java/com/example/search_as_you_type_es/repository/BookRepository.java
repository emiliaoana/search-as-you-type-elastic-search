package com.example.search_as_you_type_es.repository;

import com.example.search_as_you_type_es.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByCategory(String category);
}
