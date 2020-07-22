package com.reactive.beta.version;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class ReactiveServiceExample {

    @Bean
    public RouterFunction<?> helloRouterFunction(){
        return RouterFunctions.route(
                RequestPredicates.GET("/hello"),
                serverRequest -> ServerResponse.ok().body(Mono.just("Hello service!"), String.class)
                );
    }

}
