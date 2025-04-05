package com.example.springpracticeresttemplate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = "pageable")
public class BeerDTOPageImpl extends PageImpl<BeerDTO> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BeerDTOPageImpl(
            @JsonProperty("content") List<BeerDTO> content,
            @JsonProperty("totalElements") long total,
            @JsonProperty("size") int size,
            @JsonProperty("number") int page
    ) {
        super(content, PageRequest.of(page, size), total);
    }

    public BeerDTOPageImpl(List<BeerDTO> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public BeerDTOPageImpl(List<BeerDTO> content) {
        super(content);
    }





    /*

    this commented code is used when we used https://github.com/noobdevsam/spring-practice-data-rest.git as rest server

    @JsonCreator
    public BeerDTOPageImpl(@JsonProperty("_embedded") EmbeddedContent embeddedContent,
                           @JsonProperty("page") PageMetadata pageMetadata) {
        super(embeddedContent.getBeer(), PageRequest.of(pageMetadata.getNumber(), pageMetadata.getSize()), pageMetadata.getTotalElements());
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class EmbeddedContent {
        private final List<BeerDTO> beer;

        @JsonCreator
        EmbeddedContent(@JsonProperty("beer") List<BeerDTO> beer) {
            this.beer = beer;
        }

    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class PageMetadata {
        private final int number;
        private final int size;
        private final long totalElements;

        @JsonCreator
        PageMetadata(@JsonProperty("number") int number,
                     @JsonProperty("size") int size,
                     @JsonProperty("totalElements") long totalElements) {
            this.number = number;
            this.size = size;
            this.totalElements = totalElements;
        }

    }*/
}



