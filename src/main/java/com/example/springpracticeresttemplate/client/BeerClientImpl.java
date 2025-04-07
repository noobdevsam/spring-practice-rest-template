package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.model.BeerDTO;
import com.example.springpracticeresttemplate.model.BeerDTOPageImpl;
import com.example.springpracticeresttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    // Define the URL for the API endpoint
    private static final String GET_BEER_PATH = "/api/v1/beer";
    private static final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";

    @Override
    public Page<BeerDTO> listBeers() {
        return this.listBeers(null, null, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeers(
            String beerName,
            BeerStyle beerStyle,
            Boolean showInventory,
            Integer pageNumber,
            Integer pageSize
    ) {

        // Create a RestTemplate instance
        var restTemplate = restTemplateBuilder.build();
        var builder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

        if (beerName != null && !beerName.isEmpty()) {
            // Add the beer name as a query parameter if it's not null or empty
            builder.queryParam("beerName", beerName);
        }

        if (beerStyle != null) {
            // Add the beer style as a query parameter if it's not null
            builder.queryParam("beerStyle", beerStyle.name());
        }

        if (showInventory != null) {
            // Add the show inventory as a query parameter if it's not null
            builder.queryParam("showInventory", showInventory);
        }

        if (pageNumber != null) {
            // Add the page number as a query parameter if it's not null
            builder.queryParam("pageNumber", pageNumber);
        }

        if (pageSize != null) {
            // Add the page size as a query parameter if it's not null
            builder.queryParam("pageSize", pageSize);
        }

        // Get the response as a BeerDTOPageImpl and bound it to a ResponseEntity
        var response = restTemplate.getForEntity(builder.toUriString(), BeerDTOPageImpl.class);

        return response.getBody();
    }

    @Override
    public BeerDTO getBeerById(UUID beerId) {
        var restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(GET_BEER_BY_ID_PATH, BeerDTO.class, beerId);
    }

    @Override
    public BeerDTO createBeer(BeerDTO newBeerDTO) {
        var restTemplate = restTemplateBuilder.build();
        var uri = restTemplate.postForLocation(GET_BEER_PATH, newBeerDTO);
        assert uri != null;
        log.info("Beer created at location: {}", uri.getPath());
        return restTemplate.getForObject(uri.getPath(), BeerDTO.class);
    }

    @Override
    public BeerDTO updateBeer(BeerDTO beerDTO) {
        var restTemplate = restTemplateBuilder.build();
        restTemplate.put(GET_BEER_BY_ID_PATH, beerDTO, beerDTO.getId());
        log.debug("Beer updated at location: {}", GET_BEER_BY_ID_PATH);
        return getBeerById(beerDTO.getId());
    }
}
