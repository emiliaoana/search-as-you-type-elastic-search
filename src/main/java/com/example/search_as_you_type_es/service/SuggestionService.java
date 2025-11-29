package com.example.search_as_you_type_es.service;

import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import com.example.search_as_you_type_es.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestionService {
    private final ElasticsearchOperations elasticsearchOperations;
        public List<Book> searchAsYouType (String input){
            if (input == null || input.trim().isEmpty()) {
                return List.of();
            }

            NativeQuery query = NativeQuery.builder()
                    .withQuery(q -> q
                            .multiMatch(m -> m
                                    .query(input)
                                    .fields("title", "author", "category")
                                    .fuzziness("AUTO")
                                    .type(TextQueryType.MostFields)
                            )
                    )
                    .build();

            SearchHits<Book> searchHits = elasticsearchOperations.search(query, Book.class);

            return searchHits.stream()
                    .map(SearchHit::getContent)
                    .toList();
        }
    }
