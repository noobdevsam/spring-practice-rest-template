package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.model.BeerDTO;
import com.example.springpracticeresttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {

    Page<BeerDTO> listBeers();

    Page<BeerDTO> listBeers(
            String beerName,
            BeerStyle beerStyle,
            Boolean showInventory,
            Integer pageNumber,
            Integer pageSize
    );

    BeerDTO getBeerById(UUID beerId);

    BeerDTO createBeer(BeerDTO newBeerDTO);

    BeerDTO updateBeer(BeerDTO beerDTO);

}
