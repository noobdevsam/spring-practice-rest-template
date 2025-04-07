package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.model.BeerDTO;
import com.example.springpracticeresttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

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

    @Test
    void testCreateBeer() {

        var beerDTO = BeerDTO.builder()
                .beerName("Test Beer")
                .beerStyle(BeerStyle.IPA)
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        //var savedBeerDTO = beerClient.createBeer(beerDTO);
        //System.out.println("New record id: " + savedBeerDTO.getId());
        //assertNotNull(savedBeerDTO);

    }
}