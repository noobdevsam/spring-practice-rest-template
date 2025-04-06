package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void listBeers() {

        beerClient.listBeers("ALE", null, null, null, null);

    }

    @Test
    void listBeersNoBeerName() {

        beerClient.listBeers(null, null, null, null, null);

    }

    @Test
    void testGetById() {
        Page<BeerDTO> beerDTOs = beerClient.listBeers();
        BeerDTO beerDTO = beerDTOs.getContent().getFirst();

        BeerDTO beerDT_O1 = beerClient.getBeerById(beerDTO.getId());

        assertNotNull(beerDT_O1);

    }
}