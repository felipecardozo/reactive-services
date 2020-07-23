package com.reactive.beta.version.service;

import com.reactive.beta.version.domain.Planet;
import com.reactive.beta.version.exception.PlanetNotFoundException;
import com.reactive.beta.version.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PlanetService {

    @Autowired
    private WebClient webClient;
    private final static String URI = "/planets/{id}/";

    public Mono<Planet> findPlanet(Integer id){
        return webClient
                .get()
                .uri(URI, id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    return Mono.just(new ResourceNotFoundException("planet not found"));
                })
                .bodyToMono(Planet.class);
    }

}
