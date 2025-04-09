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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withAccepted;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

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

    @Mock
    RestTemplateBuilder mockRestTemplateBuilder = new RestTemplateBuilder(
            new MockServerRestTemplateCustomizer()
    );

    @BeforeEach
    void setUp() {
        var restTemplate = restTemplateBuilderConfigured.build();
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();

        when(
                mockRestTemplateBuilder.build()
        ).thenReturn(
                restTemplate
        );

        beerClient = new BeerClientImpl(restTemplate);

        // The setup method initializes the MockRestServiceServer and the BeerClient
        // with a RestTemplate that is configured to use the mock server.
    }

    @Test
    void test_list_beers() throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(getPage());

        mockRestServiceServer.expect(method(HttpMethod.GET))
                .andExpect(requestTo(base_url + BeerClientImpl.GET_BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> dtos = beerClient.listBeers();

        assertThat(dtos.getContent().size()).isGreaterThan(0);
    }

    @Test
    void test_get_by_id() throws JsonProcessingException {
        var dto = getBeerDTO();
        var response = objectMapper.writeValueAsString(dto);

        mockRestServiceServer
                .expect(method(HttpMethod.GET))
                .andExpect(
                        requestToUriTemplate(base_url + BeerClientImpl.GET_BEER_BY_ID_PATH,
                                dto.getId())
                ).andRespond(
                        withSuccess(response, MediaType.APPLICATION_JSON)
                );

        var responseDto = beerClient.getBeerById(dto.getId());
        assertThat(responseDto.getId()).isEqualTo(dto.getId());
    }

    @Test
    void test_create() throws JsonProcessingException {
        var dto = getBeerDTO();
        var response = objectMapper.writeValueAsString(dto);
        var uri = UriComponentsBuilder.fromPath(BeerClientImpl.GET_BEER_BY_ID_PATH)
                .build(dto.getId());

        mockRestServiceServer
                .expect(method(HttpMethod.POST))
                .andExpect(
                        requestTo(base_url + BeerClientImpl.GET_BEER_PATH)
                ).andRespond(
                        withAccepted().location(uri)
                );

        mockRestServiceServer
                .expect(method(HttpMethod.GET))
                .andExpect(
                        requestToUriTemplate(base_url + BeerClientImpl.GET_BEER_BY_ID_PATH,
                                dto.getId())
                ).andRespond(
                        withSuccess(response, MediaType.APPLICATION_JSON)
                );

        var responseDto = beerClient.createBeer(dto);
        assertThat(responseDto.getId()).isEqualTo(dto.getId());
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