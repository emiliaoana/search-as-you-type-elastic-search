package com.example.search_as_you_type_es.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "books")
@Setting(settingPath = "es-settings.json")
public class Book {
    @Id
    private String id;
    @Field(type = FieldType.Text,
            analyzer = "autocomplete_index",
            searchAnalyzer = "autocomplete_search")
    private String title;

    @Field(type = FieldType.Text,
            analyzer = "autocomplete_index",
            searchAnalyzer = "autocomplete_search")
    private String author;

    @Field(type = FieldType.Keyword)
    private String isbn;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text,
            analyzer = "autocomplete_index",
            searchAnalyzer = "autocomplete_search")
    private String category;

    @Field(type = FieldType.Double)
    private Double price;
}
