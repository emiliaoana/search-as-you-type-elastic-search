package com.example.search_as_you_type_es.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import com.example.search_as_you_type_es.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.response.CompletionSuggestion;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<String> getSuggestions(String query) {
        NativeQuery searchQuery = NativeQuery.builder()
            .withQuery(Query.of(q -> q.matchAll(m -> m)))
            .withSuggester(Suggester.of(s -> s
                .suggesters("book-suggest", fs -> fs
                    .prefix(query)
                    .completion(c -> c
                        .field("suggest")
                        .size(10)
                        .skipDuplicates(true)
                    )
                )
            ))
            .build();

        SearchHits<Book> searchHits = elasticsearchOperations.search(searchQuery, Book.class);
        Suggest suggest = searchHits.getSuggest();
        
        if (suggest == null) {
            return new ArrayList<>();
        }

        CompletionSuggestion<String> completionSuggestion = (CompletionSuggestion<String>) suggest.getSuggestion("book-suggest");

        return completionSuggestion.getEntries().stream()
            .flatMap(entry -> ((CompletionSuggestion.Entry) entry).getOptions().stream())
            .map(option -> ((CompletionSuggestion.Entry.Option) option).getText().toString())
            .distinct()
                .toList();
    }
}
