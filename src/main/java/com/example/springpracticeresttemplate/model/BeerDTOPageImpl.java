package com.example.springpracticeresttemplate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

//@JsonIgnoreProperties(ignoreUnknown = true, value = "pageable")
public class BeerDTOPageImpl extends PageImpl<BeerDTO> {

    @JsonCreator
    public BeerDTOPageImpl(@JsonProperty("_embedded") EmbeddedContent embeddedContent,
                           @JsonProperty("page") PageMetadata pageMetadata) {
        super(embeddedContent.beer(), PageRequest.of(pageMetadata.number(), pageMetadata.size()), pageMetadata.totalElements());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record EmbeddedContent(List<BeerDTO> beer) {
        @JsonCreator
        EmbeddedContent(@JsonProperty("beer") List<BeerDTO> beer) {
            this.beer = beer;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record PageMetadata(int number, int size, long totalElements) {
        @JsonCreator
        PageMetadata(@JsonProperty("number") int number,
                     @JsonProperty("size") int size,
                     @JsonProperty("totalElements") long totalElements) {
            this.number = number;
            this.size = size;
            this.totalElements = totalElements;
        }
    }
}



