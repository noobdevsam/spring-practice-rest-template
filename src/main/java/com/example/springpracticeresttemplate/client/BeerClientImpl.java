package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.model.BeerDTO;
import com.example.springpracticeresttemplate.model.BeerDTOPageImpl;
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

        // Create a RestTemplate instance
        RestTemplate restTemplate = restTemplateBuilder.build();
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

        // Get the response as a BeerDTOPageImpl and bound it to a ResponseEntity
        ResponseEntity<BeerDTOPageImpl> response = restTemplate.getForEntity(builder.toUriString(), BeerDTOPageImpl.class);

        return response.getBody();
    }

}
