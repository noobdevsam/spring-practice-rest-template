package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.config.RestTemplateConfig;
import com.example.springpracticeresttemplate.model.BeerDTO;
import com.example.springpracticeresttemplate.model.BeerDTOPageImpl;
import com.example.springpracticeresttemplate.model.BeerStyle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(BeerClientImpl.class)
@Import(RestTemplateConfig.class)
class BeerClientMockTest {

    static final String base_url = "http://localhost:9090";

    @Autowired
    BeerClient beerClient;

    @Autowired
    MockRestServiceServer mockRestServiceServer;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void test_list_beers() throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(getPage());

        mockRestServiceServer.expect(method(HttpMethod.GET))
                .andExpect(requestTo(base_url + BeerClientImpl.GET_BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> dtos = beerClient.listBeers();

        assertThat(dtos.getContent().size()).isGreaterThan(0);
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