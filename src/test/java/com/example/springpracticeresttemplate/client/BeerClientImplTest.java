package com.example.springpracticeresttemplate.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void listBeers() {

        beerClient.listBeers("ALE");

    }

    @Test
    void listBeersNoBeerName() {

        beerClient.listBeers(null);

    }
}