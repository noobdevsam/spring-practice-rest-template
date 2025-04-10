package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.config.RestTemplateConfig;
import com.example.springpracticeresttemplate.model.BeerDTO;
import com.example.springpracticeresttemplate.model.BeerDTOPageImpl;
import com.example.springpracticeresttemplate.model.BeerStyle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest
@Import(RestTemplateConfig.class)
class BeerClientMockTest {

    static final String base_url = "http://localhost:9090";

    BeerClient beerClient;

    MockRestServiceServer mockRestServiceServer;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplateBuilder restTemplateBuilderConfigured;

    BeerDTO dto;
    String dtoJson;

    @Mock
    RestTemplateBuilder mockRestTemplateBuilder = new RestTemplateBuilder(
            new MockServerRestTemplateCustomizer()
    );

    @BeforeEach
    void setUp() throws JsonProcessingException {
        var restTemplate = restTemplateBuilderConfigured.build();
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();

        when(
                mockRestTemplateBuilder.build()
        ).thenReturn(
                restTemplate
        );

        beerClient = new BeerClientImpl(restTemplate);

        dto = getBeerDTO();
        dtoJson = objectMapper.writeValueAsString(dto);

    }

    @Test
    void test_list_beers() throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(getPage());

        mockRestServiceServer
                .expect(method(HttpMethod.GET))
                .andExpect(
                        requestTo(base_url + BeerClientImpl.GET_BEER_PATH)
                ).andRespond(
                        withSuccess(payload, MediaType.APPLICATION_JSON)
                );

        var dtos = beerClient.listBeers();

        assertThat(dtos.getContent().size()).isGreaterThan(0);
    }

    @Test
    void test_get_by_id() {

        mockGetOperation();

        var responseDto = beerClient.getBeerById(dto.getId());
        assertThat(responseDto.getId()).isEqualTo(dto.getId());
    }

    @Test
    void test_create_beer() {
        var uri = UriComponentsBuilder.fromPath(BeerClientImpl.GET_BEER_BY_ID_PATH)
                .build(dto.getId());

        mockRestServiceServer
                .expect(method(HttpMethod.POST))
                .andExpect(
                        requestTo(base_url + BeerClientImpl.GET_BEER_PATH)
                ).andRespond(
                        withAccepted().location(uri)
                );

        mockGetOperation();

        var responseDto = beerClient.createBeer(dto);
        assertThat(responseDto.getId()).isEqualTo(dto.getId());
    }

    @Test
    void test_update_beer() {
        mockRestServiceServer
                .expect(method(HttpMethod.PUT))
                .andExpect(
                        requestToUriTemplate(base_url + BeerClientImpl.GET_BEER_BY_ID_PATH,
                                dto.getId())
                ).andRespond(
                        withNoContent()
                );

        mockGetOperation();

        var responseDto = beerClient.updateBeer(dto);
        assertThat(responseDto.getId()).isEqualTo(dto.getId());
    }

    @Test
    void test_delete_beer() {
        mockRestServiceServer
                .expect(method(HttpMethod.DELETE))
                .andExpect(
                        requestToUriTemplate(base_url + BeerClientImpl.GET_BEER_BY_ID_PATH,
                                dto.getId())
                ).andRespond(
                        withNoContent()
                );

        beerClient.deleteBeerById(dto.getId());

        mockRestServiceServer.verify(); // Verify that the DELETE request was made
    }

    @Test
    void test_delete_beer_not_found() {
        mockRestServiceServer
                .expect(method(HttpMethod.DELETE))
                .andExpect(
                        requestToUriTemplate(base_url + BeerClientImpl.GET_BEER_BY_ID_PATH,
                                dto.getId())
                ).andRespond(
                        withResourceNotFound()
                );

        assertThrows(
                HttpClientErrorException.class, () -> beerClient.deleteBeerById(dto.getId())
        );

        mockRestServiceServer.verify(); // Verify that the DELETE request was made
    }

    private void mockGetOperation() {
        mockRestServiceServer
                .expect(method(HttpMethod.GET))
                .andExpect(
                        requestToUriTemplate(base_url + BeerClientImpl.GET_BEER_BY_ID_PATH,
                                dto.getId())
                ).andRespond(
                        withSuccess(dtoJson, MediaType.APPLICATION_JSON)
                );
    }

    BeerDTOPageImpl getPage() {
        return new BeerDTOPageImpl(Collections.singletonList(getBeerDTO()), 1, 25, 1);
    }

    BeerDTO getBeerDTO() {
        return BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName("Nerd Beer")
                .beerStyle(BeerStyle.IPA)
                .upc("656554")
                .price(new BigDecimal("456.23"))
                .quantityOnHand(1252)
                .build();
    }
}