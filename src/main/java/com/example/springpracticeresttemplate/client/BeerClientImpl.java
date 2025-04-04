package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.model.BeerDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    // Define the URL for the API endpoint
    private static final String BASE_URL = "http://localhost:9090";
    private static final String GET_BEER_PATH = "/api/v1/beer";

    @Override
    public Page<BeerDTO> listBeers() {

        // Create a RestTemplate instance
        RestTemplate restTemplate = restTemplateBuilder.build();

        // Make the API call and get the response
        // Get the response as a String and bound it to a ResponseEntity
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, String.class);

        // Get the response as a Map and bound it to a ResponseEntity
        // Using Map to represent the JSON response which is good if we don't know the structure/values of the response
        ResponseEntity<Map> map_response = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, Map.class);

        // Get the response as a JsonNode and bound it to a ResponseEntity
        ResponseEntity<JsonNode> json_response = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, JsonNode.class);

        // Print a specific field from the JSON response
        Objects.requireNonNull(json_response.getBody()).findPath("beer")
                .elements().forEachRemaining(node -> System.out.println(node.get("beerName").asText()));

        System.out.println(response.getBody());

        return null;
    }

}
