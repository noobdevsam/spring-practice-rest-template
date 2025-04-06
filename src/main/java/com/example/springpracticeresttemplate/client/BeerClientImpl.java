package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.model.BeerDTO;
import com.example.springpracticeresttemplate.model.BeerDTOPageImpl;
import com.example.springpracticeresttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    // Define the URL for the API endpoint
    private static final String GET_BEER_PATH = "/api/v1/beer";

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
        RestTemplate restTemplate = restTemplateBuilder.build();
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

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
        ResponseEntity<BeerDTOPageImpl> response = restTemplate.getForEntity(builder.toUriString(), BeerDTOPageImpl.class);

        return response.getBody();
    }

}
