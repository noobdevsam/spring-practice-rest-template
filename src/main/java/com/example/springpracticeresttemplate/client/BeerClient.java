package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.model.BeerDTO;
import com.example.springpracticeresttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

public interface BeerClient {

    Page<BeerDTO> listBeers();

    Page<BeerDTO> listBeers(
            String beerName,
            BeerStyle beerStyle,
            Boolean showInventory,
            Integer pageNumber,
            Integer pageSize
    );

}
