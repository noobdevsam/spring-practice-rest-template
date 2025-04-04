package com.example.springpracticeresttemplate.client;

import com.example.springpracticeresttemplate.model.BeerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;

    @Override
    public Page<BeerDTO> listBeers() {

        // Create a RestTemplate instance
        RestTemplate restTemplate = restTemplateBuilder.build();

        // Define the URL for the API endpoint
        String api_url = "http://localhost:9090/api/v1/beer";

        // Make the API call and get the response
        // Get the response as a String and bound it to a ResponseEntity
        ResponseEntity<String> response = restTemplate.getForEntity(api_url, String.class);

        // Get the response as a Map and bound it to a ResponseEntity
        // Using Map to represent the JSON response which is good if we don't know the structure/values of the response
        ResponseEntity<Map> map_response = restTemplate.getForEntity(api_url, Map.class);

        System.out.println(response.getBody());

        return null;
    }

}
