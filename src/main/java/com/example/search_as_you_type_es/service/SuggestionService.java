package com.example.search_as_you_type_es.service;

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
    public List<String> getSuggestions(String input) {
        String suggesterName = "book-suggest";

        NativeQuery searchQuery = NativeQuery.builder()
                .withSuggester(Suggester.of(s -> s
                        .suggesters(suggesterName, fs -> fs
                                .prefix(input)
                                .completion(c -> c
                                        .field("suggest")
                                        .size(10)
                                        .skipDuplicates(true)
                                        .fuzzy(f -> f
                                                .fuzziness("AUTO")
                                                .minLength(3)
                                        )
                                )
                        )
                ))
                .build();

        SearchHits<Book> searchHits = elasticsearchOperations.search(searchQuery, Book.class);
        Suggest suggestResult = searchHits.getSuggest();

        if (suggestResult == null) {
            return new ArrayList<>();
        } else {
            suggestResult.getSuggestion(suggesterName);
        }

        CompletionSuggestion<?> suggestion = (CompletionSuggestion<?>) suggestResult.getSuggestion(suggesterName);

        return suggestion.getEntries().stream()
                .flatMap(entry -> entry.getOptions().stream())
                .map(Suggest.Suggestion.Entry.Option::getText)
                .distinct()
                .toList();
    }

}