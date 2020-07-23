package com.reactive.beta.version;

import com.reactive.beta.version.domain.Planet;
import com.reactive.beta.version.exception.ResourceNotFoundException;
import com.reactive.beta.version.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Configuration
public class ReactiveServiceExample {

    @Autowired
    private PlanetService planetService;
    private final String URL = "https://swapi.dev/api/";

    @Bean
    public WebClient webClient(){
        return WebClient.create(URL);
    }

    @Bean
    public RouterFunction<?> helloRouterFunction(){
        return RouterFunctions.route(
                RequestPredicates.GET(
                        "/hello"),
                        serverRequest -> ServerResponse.ok().body(Mono.just("Hello service!"), String.class))
                .andRoute( RequestPredicates.GET("/planets/{idPlanet}"), this::findPlanet)
                .filter(resourceNotFound());
    }

    public Mono<ServerResponse> findPlanet(ServerRequest request){
        Integer pathId = Integer.parseInt( request.pathVariable("idPlanet") );
        return ServerResponse.ok().body(
                planetService.findPlanet( pathId ),
                Planet.class
        );
    }

    private HandlerFilterFunction<ServerResponse, ServerResponse> resourceNotFound(){
        return (request, next) -> next.handle(request).log()
                .onErrorResume(ResourceNotFoundException.class, e -> ServerResponse.badRequest().build());
    }

}
